/*
	Copyright 2005 Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.jbel.function.math;

import java.math.BigDecimal;

import com.strategicgains.jbel.exception.FunctionException;
import com.strategicgains.jbel.function.BinaryFunction;

public class DivideFunction
	extends MathFunction
	implements BinaryFunction
{
	public Object perform(Object argument1, Object argument2)
		throws FunctionException
	{
		return (getBigDecimalValue(argument1)).divide(getBigDecimalValue(argument2), BigDecimal.ROUND_HALF_UP);
	}
}
