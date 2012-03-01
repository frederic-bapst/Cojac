/*
 * *
 *    Copyright 2011 Baptiste Wicht & Frédéric Bapst
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

package ch.eiafr.cojac.unit;

import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Args;
import ch.eiafr.cojac.CojacReferences.CojacReferencesBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class LogFileTest {
    @Test
    public void testLogFileExists() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
        NoSuchMethodException, InvocationTargetException {
        String logFile = System.getProperty("user.dir") + "/test.log";

        Args args = new Args();

        args.specify(Arg.ALL);
        args.setValue(Arg.LOG_FILE, logFile);
/* TODO
        CojacClassLoader classLoader = new CojacClassLoader(new URL[]{}, new CojacReferencesBuilder(args));

        Class<?> classz = classLoader.loadClass("ch.eiafr.cojac.unit.SimpleOverflows");

        Object object = classz.newInstance();

        Method m = classz.getMethod("test");

        m.invoke(object);

        Assert.assertTrue(new File(logFile).exists());
        Assert.assertTrue(new File(logFile).length() > 0);

        new File(logFile).delete();
*/
    }
}