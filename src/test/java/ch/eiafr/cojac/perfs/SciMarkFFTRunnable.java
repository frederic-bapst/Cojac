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

package ch.eiafr.cojac.perfs;

import ch.eiafr.cojac.perfs.scimark.Constants;
import ch.eiafr.cojac.perfs.scimark.Kernel;
import ch.eiafr.cojac.perfs.scimark.Random;

public class SciMarkFFTRunnable implements Runnable {
    @Override
    public void run() {
        Kernel.measureFFT(Constants.FFT_SIZE, Constants.RESOLUTION_DEFAULT, new Random());
    }
}