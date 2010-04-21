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
package com.strategicgains.jbel.predicate;

import java.util.Collection;

import com.strategicgains.jbel.expression.Expression;

public class IsInPredicate
	extends PredicateExpression
{
	public IsInPredicate(Expression leftExpression, Expression rightExpression)
	{
		super(leftExpression, rightExpression);
	}

	public Object evaluateResults(Object object1, Object object2)
	{
		return Boolean.valueOf(((Collection)object2).contains(object1));
	}
}
