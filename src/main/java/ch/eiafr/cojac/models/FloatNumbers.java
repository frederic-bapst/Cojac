/*
 * *
 *    Copyright 2014 Frédéric Bapst & Romain Monnard
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ch.eiafr.cojac.models;

import static ch.eiafr.cojac.models.FloatReplacerClasses.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class FloatNumbers {
	
	public static Object[] newarray(int size) throws Exception{
		Object[] a = (Object[]) Array.newInstance(COJAC_FLOAT_WRAPPER_CLASS, size);
        for (int i = 0; i < a.length; i++) {
            a[i] = COJAC_FLOAT_WRAPPER_CLASS.getConstructor(float.class).newInstance(0);
        }
        return a;
    }

    
    public static Object initializeMultiArray(Object array, int dimensions) throws Exception {
        Object a[] = (Object[]) array;
        if(dimensions == 1)
            return newarray(a.length);
        for (int i = 0; i < a.length; i++)
            a[i] = initializeMultiArray(a[i], dimensions-1);
        return array;
    }
    
	private static Object[] convertArray(float[] array) throws Exception{
        Object[] a = (Object[]) Array.newInstance(COJAC_FLOAT_WRAPPER_CLASS, array.length);
        for (int i = 0; i < a.length; i++)
            a[i] = COJAC_FLOAT_WRAPPER_CLASS.getConstructor(float.class).newInstance(array[i]);
        return a;
    }
	
	private static float[] convertArray(Object[] array) throws Exception{
        float[] a = new float[array.length];
        for (int i = 0; i < a.length; i++){
			Method m = COJAC_FLOAT_WRAPPER_CLASS.getMethod("toFloat", new Class[] {COJAC_FLOAT_WRAPPER_CLASS});
			a[i] = (float)m.invoke(COJAC_FLOAT_WRAPPER_CLASS, array[i]);
		}
        return a;
    }
	
	// Get the Type of an array of type compClass with the number of dimensions
    private static Class<?> arrayClass(Class<?> compClass, int dimensions) {
        if (dimensions == 0) {
            return compClass;
        }
        int[] dims = new int[dimensions];
        Object dummy = Array.newInstance(compClass, dims);
        return dummy.getClass();
    }

    public static Object convertArrayToReal(Object array, int dimensions) throws Exception {
        Object a;
		Object[] input = (Object[])array;
        if(dimensions == 1){
            a = convertArray(input);
        }
        else{
            Class<?> compType = arrayClass(float.class, dimensions - 1);
            a = Array.newInstance(compType, input.length);
            Object[] b = (Object[]) a; // All arrays or multi-arrays can be cast to Object[]
            for (int i = 0; i < b.length; i++) {
                b[i] = convertArrayToReal(input[i], dimensions-1); // Initialise the others dimensions
            }
        }
        return a;
    }
	
	public static Object convertArrayToCojac(Object array, int dimensions) throws Exception {
        Object a;
        if(dimensions == 1){
            a = convertArray((float[])array);
        }
        else{
			Object[] input = (Object[])array;
            Class<?> compType = arrayClass(COJAC_FLOAT_WRAPPER_CLASS, dimensions - 1);
            a = Array.newInstance(compType, input.length);
            Object[] b = (Object[]) a; // All arrays or multi-arrays can be cast to Object[]
            for (int i = 0; i < b.length; i++) {
                b[i] = convertArrayToCojac(input[i], dimensions-1); // Initialise the others dimensions
            }
        }
        return a;
    }
	 /*
	 // Not the good way
    public static Object convertArrayToCojac(Object array, int dimensions){
        Object a[] = (Object[]) array;
        if(dimensions == 1)
            return convertArray((float[])array);
        for (int i = 0; i < a.length; i++)
            a[i] = convertArrayToReal(a[i], dimensions-1);
        return array;
    }
*/	
	public static Object initialize(Object a) throws Exception{
		if(a == null)
			return COJAC_FLOAT_WRAPPER_CLASS.getConstructor(float.class).newInstance(0);
		return a;
	}

	public static Object castFromObject(Object obj) throws Exception{
		if(obj instanceof Double)
			return COJAC_FLOAT_WRAPPER_CLASS.getConstructor(float.class).newInstance((Double)obj);
		return obj;
	}
	
	
	
}
