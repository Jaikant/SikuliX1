/*
 * Copyright (c) 2010-2018, sikuli.org, sikulix.com - MIT license
 */
package org.sikuli.script;

import java.io.PipedInputStream;
import java.net.URI;
import java.util.Map;

/**
 * Interface for ScriptRunners like Jython.
 */
public interface IScriptRunner {

  public class Options {
    private boolean silent = false;
    private int errorLine = -1;

    public boolean isSilent() {
      return silent;
    }

    public Options setSilent(boolean silent) {
      this.silent = silent;
      return this;
    }

    public int getErrorLine() {
      return errorLine;
    }

    public void setErrorLine(int errorLine) {
      this.errorLine = errorLine;
    }
  }

  /**
   * Can be used to initialize the ScriptRunner. This method is called at the beginning of program
   * execution. The given parameters can be used to parse any ScriptRunner specific custom options.
   *
   * @param args All arguments that were passed to the main-method
   */
  public void init(String[] args) throws SikuliXception;

  /**
   * Executes the Script.
   *
   * @param scriptFile Identifier pointing to the script. This can either by a file path
   *                   or an URI, depending on the runner implementation
   * @param scriptArgs Arguments to be passed directly to the script with --args
   * @param options Implementation specific options.
   * @return exitcode for the script execution
   */
  public int runScript(String scriptFile, String[] scriptArgs, Options options);

  /**
   * Evaluates the Script.
   *
   * @param script Script content
   * @param options Implementation specific options.
   * @return exitcode for the script execution
   */
  public int evalScript(String script, Options options);

  /**
   * Run the given script lines.
   * The implementation might perform some optimizations on
   * the code (e.g. fix indentation) before executing it.
   *
   * @param lines Code do execute
   * @param options Implementation specific options.
   */
  public void runLines(String lines, Options options);

  /**
   * Executes the Script as Test.
   *
   * @param scriptfile File containing the script
   * @param imagedirectory Directory containing the images (might be null: parent of script)
   * @param scriptArgs Arguments to be passed directly to the script with --args
   * @param options when called from Sikuli IDE additional info
   * @return exitcode for the script execution
   */
  public int runTest(URI scriptfile, URI imagedirectory, String[] scriptArgs, Options options);

  /**
   * Starts an interactive session with the scriptrunner.
   *
   * @param scriptArgs Arguments to be passed directly to the script with --args
   * @return exitcode of the interactive session
   */
  public int runInteractive(String[] scriptArgs);

  /**
   * Gets the scriptrunner specific help text to print on stdout.
   *
   * @return A helping description about how to use the scriptrunner
   */
  public String getCommandLineHelp();

  /**
   * Gets the help text that is shown if the user runs "shelp()" in interactive mode
   *
   * @return The helptext
   */
  public String getInteractiveHelp();

  /**
   * Checks if the current platform supports this runner.
   *
   * @return true, if platform supports this runner, false otherwise
   */
  public boolean isSupported();

  /**
   * Gets the name of the ScriptRunner. Should be unique. This value is needed to distinguish
   * between different ScriptRunners.
   *
   * @return Name to identify the ScriptRunner or null if not available
   */
  public String getName();

  /**
   * returns the list of possible script file extensions, first is the default
   *
   * @return array of strings
   */
  public String[] getExtensions();

  /**
   * return the type of script this handler can execute.
   *
   * @return
   */
  public String getType();

  /**
   * checks whether this ScriptRunner supports the given file extension
   *
   * @return true if the runner has the given extension, false otherwise
   */
  public boolean hasExtension(String ending);

  /**
   * Is executed before Sikuli closes. Can be used to cleanup the ScriptRunner
   */
  public void close();

  /**
   * add statements to be run after SCRIPT_HEADER, but before script is executed
   *
   * @param stmts string array of statements (null resets the statement buffer)
   */
  public void execBefore(String[] stmts);

  /**
   * add statements to be run after script has ended
   *
   * @param stmts string array of statements (null resets the statement buffer)
   */
  public void execAfter(String[] stmts);

  /**
   * Checks if this runner can handle the given identifier.
   *
   * @param identifier Can be Runner name, type or one of its supported extensions
   *                   Can also be script code prefixed with the runnerName
   *                   (e.g. JavaScriptconsolel.log("hello"))
   * @return true if the runner can handle the identifier, false otherwise
   */
  public boolean canHandle(String identifier);

  /**
   * Redirects the runner's STDIO to the given PipedInputStreams.
   *
   * Redirection can be done only once per Runner instance.
   *
   * @param stdout PipedInputStreams for STDOUT
   * @param stderr PipedInputStreams for STDERR
   *
   * @return
   */
  public void redirect(PipedInputStream stdout, PipedInputStream stderr);

  /**
   * Resets this runner.
   *
   * The runner gets closed and initialized again using init.
   */
  public void reset();
}
