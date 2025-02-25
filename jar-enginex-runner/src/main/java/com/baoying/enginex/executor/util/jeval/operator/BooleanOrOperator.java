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

package com.baoying.enginex.executor.util.jeval.operator;

/**
 * The boolean or operator.
 */
public class BooleanOrOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public BooleanOrOperator() {
		super("||", 1);
	}

	/**
	 * Evaluates two double operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	public double evaluate(final double leftOperand, final double rightOperand) {
		if (leftOperand == 1 || rightOperand == 1) {
			return 1;
		}

		return 0;
	}
}