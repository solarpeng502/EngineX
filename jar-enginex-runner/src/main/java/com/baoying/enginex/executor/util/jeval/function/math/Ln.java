package com.baoying.enginex.executor.util.jeval.function.math;

import com.baoying.enginex.executor.util.jeval.Evaluator;
import com.baoying.enginex.executor.util.jeval.function.Function;
import com.baoying.enginex.executor.util.jeval.function.FunctionConstants;
import com.baoying.enginex.executor.util.jeval.function.FunctionException;
import com.baoying.enginex.executor.util.jeval.function.FunctionResult;

/**
 * log以e为底的对数
 * 
 * @author Administrator
 *
 */
public class Ln implements Function {

	@Override
	public String getName() {
		return "ln";
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments)
			throws FunctionException {
		Double result = null;
		Double number = null;

		try {
			number = new Double(arguments);
		} catch (Exception e) {
			throw new FunctionException("Invalid argument.", e);
		}

		result = new Double(Math.log(number)/Math.log(Math.E));

		return new FunctionResult(result.toString(),
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);

	}

}
