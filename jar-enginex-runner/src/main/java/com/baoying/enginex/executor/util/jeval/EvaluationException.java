/*
 * Copyright 2002-2007 Robert Breidecker.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baoying.enginex.executor.util.jeval;

/**
 * This exception is thrown when an error occurs during the evaluation process.
 */
public class EvaluationException extends Exception {

	private static final long serialVersionUID = -3010333364122748053L;

	/**
	 * This constructor takes a custom message as input.
	 * 
	 * @param message
	 *            A custom message for the exception to display.
	 */
	public EvaluationException(String message) {
		super(message);
	}

	/**
	 * This constructor takes an exception as input.
	 * 
	 * @param exception
	 *            An exception.
	 */
	public EvaluationException(Exception exception) {
		super(exception);
	}

	/**
	 * This constructor takes an exception as input.
	 * 
	 * @param message
	 *            A custom message for the exception to display.
	 * @param exception
	 *            An exception.
	 */
	public EvaluationException(String message, Exception exception) {
		super(message, exception);
	}
}