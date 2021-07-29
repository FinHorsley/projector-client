/*
 * MIT License
 *
 * Copyright (c) 2019-2021 JetBrains s.r.o.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jetbrains.projector.server.core.classloader

import org.jetbrains.projector.server.core.ij.IjInjectorAgentInitializer
import org.jetbrains.projector.server.core.ij.invokeWhenIdeaIsInitialized
import org.jetbrains.projector.server.core.ij.md.PanelUpdater
import org.jetbrains.projector.util.loading.ProjectorClassLoader

public fun initClassLoader(classLoader: ClassLoader): ClassLoader {
  val prjClassLoader = if (classLoader is ProjectorClassLoader) classLoader else ProjectorClassLoader.instance

  // accessed in agent to get ide ana projector classloaders in platform classloader context
  prjClassLoader.forceLoadByPlatform(IjInjectorAgentInitializer.IjInjectorAgentClassLoaders::class.java.name)
  // accessed in client side markdown previewer in platform classloader context
  prjClassLoader.forceLoadByPlatform(PanelUpdater::class.java.name)
  // without this server not works...
  prjClassLoader.forceLoadByPlatform("org.jetbrains.projector.server.core.websocket.")

  invokeWhenIdeaIsInitialized("Init ProjectorClassLoader") {
    prjClassLoader.ideaClassLoader = it
  }

  return prjClassLoader
}