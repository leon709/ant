/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.taskdefs;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Abstract Base class for pack tasks.
 *
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 */

public abstract class Pack extends Task {

    protected File zipFile;
    protected File source;

    public void setZipfile(File zipFile) {
        this.zipFile = zipFile;
    }

    public void setSrc(File src) {
        source = src;
    }

    private void validate() {
        if (zipFile == null) {
            throw new BuildException("zipfile attribute is required", location);
        }

        if (source == null) {
            throw new BuildException("src attribute is required", location);
        }

        if (source.isDirectory()) {
            throw new BuildException("Src attribute must not " +
                                     "represent a directory!", location);
        }
    }

    public void execute() throws BuildException {
        validate();
        log("Building: " + zipFile.getAbsolutePath());
        pack();
    }

    private void zipFile(InputStream in, OutputStream zOut)
        throws IOException
    {
        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }

    protected void zipFile(File file, OutputStream zOut)
        throws IOException
    {
        FileInputStream fIn = new FileInputStream(file);
        try {
            zipFile(fIn, zOut);
        } finally {
            fIn.close();
        }
    }

    protected abstract void pack();
}
