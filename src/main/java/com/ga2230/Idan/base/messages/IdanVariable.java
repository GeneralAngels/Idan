/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages;

import com.ga2230.Idan.base.utils.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Inherit from this class to create new custom variables
 */
public abstract class IdanVariable {
    private static Boolean hadErrors = false;

    // To get rid of the reference
    // Straight from StackOverflow (With modifications)
    // Link: https://stackoverflow.com/a/25338780
    private Object clone(Object obj){
        try{
            Object dummy = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(dummy, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(dummy, dummy);
                    }else{
                        field.set(dummy, clone(field.get(obj)));
                    }
                }
            }
            return dummy;
        } catch (InstantiationException e) {
            if (IdanVariable.hadErrors){
                Logger.log("REFERENCE ERROR", "Could not clone the " + obj.getClass().getName() + " object. " +
                        "KEEPING THE REFERENCE, TO STOP IT ADD A NO-ARG CONSTRUCTOR");
                IdanVariable.hadErrors = true;
            }
            return obj;
        } catch(Exception e){
            return null;
        }
    }

    // Cloning the object
    public Object clone(){
        return clone(this);
    }
}
