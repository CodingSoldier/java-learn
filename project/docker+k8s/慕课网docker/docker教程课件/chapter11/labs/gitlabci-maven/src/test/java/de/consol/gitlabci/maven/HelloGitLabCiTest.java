package de.consol.gitlabci.maven;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple HelloGitLabCi.
 */
public class HelloGitLabCiTest
    extends TestCase
{
    /**
     * Rigourous Test :-)
     */
    public void testSayHello()
    {
        assertEquals(HelloGitLabCi.sayHello(), "Hello GitLab CI!");
    }
}
