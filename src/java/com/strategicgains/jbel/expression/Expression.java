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
package com.strategicgains.jbel.expression;

import com.strategicgains.jbel.exception.EvaluationException;

/**
 * Expression is the root object of the expression library.  It follows the Composite pattern (GoF), in that
 * an expression is made up of sub-expressions.
 * 
 * @author Todd Fredrich
 * @since Aug 22, 2005
 * @version $Revision$
 */
public interface Expression
{
	public Object evaluate(Object argument)
	throws EvaluationException;
}
