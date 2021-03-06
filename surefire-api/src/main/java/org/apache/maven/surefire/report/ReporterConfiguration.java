package org.apache.maven.surefire.report;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Timer;

/**
 * @author Kristian Rosenvold
 */
public class ReporterConfiguration
{
    private final List reports;

    private final File reportsDirectory;

    private final PrintStream originalSystemOut;

    private final PrintStream originalSystemErr;

    private final Integer forkTimeout;

    /**
     * A non-null Boolean value
     */
    private final Boolean trimStackTrace;

    private volatile boolean timedOut = false;


    public ReporterConfiguration( List reports, File reportsDirectory, Boolean trimStackTrace, Integer forkWithTimeout )
    {
        this.reports = reports;
        this.reportsDirectory = reportsDirectory;
        this.trimStackTrace = trimStackTrace;
        this.forkTimeout = forkWithTimeout;
        /*
        * While this may seem slightly odd, when this object is constructted no user code has been run
        * (including classloading), and we can be guaranteed that no-one has modified System.out/System.err.
        * As soon as we start loading user code, all h*ll breaks loose in this respect.
         */
        this.originalSystemOut = System.out;
        this.originalSystemErr = System.err;

    }

    // todo: remove once we build with 2.7.2
    public ReporterConfiguration( List reports, File reportsDirectory, Boolean trimStackTrace )
    {
        this( reports, reportsDirectory, trimStackTrace, null );
    }

    /**
     * The directory where reports will be created, normally ${project.build.directory}/surefire-reports
     *
     * @return A file pointing at the specified directory
     */
    public File getReportsDirectory()
    {
        return reportsDirectory;
    }

    /**
     * Indicates if reporting should trim the stack traces.
     *
     * @return true if stacktraces should be trimmed in reporting
     */
    public Boolean isTrimStackTrace()
    {
        return trimStackTrace;
    }

    /**
     * A list of classnames representing runnable reports for this test-run.
     *
     * @return A list of strings, each string is a classname of a class
     *         implementing the org.apache.maven.surefire.report.Reporter interface
     */
    public List getReports()
    {
        return reports;
    }

    /**
     * The original system out belonging to the (possibly forked) surefire process.
     * Note that users of Reporter/ReporterFactory should normally not be using this.
     *
     * @return A printstream.
     */
    public PrintStream getOriginalSystemOut()
    {
        return originalSystemOut;
    }

    /**
     * The original system err belonging to the (possibly forked) surefire process.
     * Note that users of Reporter/ReporterFactory should normally not be using this.
     *
     * @return A printstream.
     */
    public PrintStream getOriginalSystemErr()
    {
        return originalSystemErr;
    }

    /**
     * Indicates that the test is running timed out, meaning this process could be abruptly killed.
     * This will normally tell the reporter to delete result files upon startup.
     *
     * @return true if test run can be killed by timeout
     */
    public boolean isForkWithTimeout()
    {
        return getForkTimeout() != null;
    }

    public Integer getForkTimeout()
    {
        return forkTimeout;
    }

    public void setTimedOut( boolean timedOut )
    {
        this.timedOut = timedOut;
    }

    public boolean isTimedOut()
    {
        return this.timedOut;
    }
}
