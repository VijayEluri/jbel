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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.strategicgains.jbel.CollationOrder;
import com.strategicgains.jbel.exception.EvaluationException;
import com.strategicgains.jbel.function.ComparingFunction;

/**
 * An CollationExpression facilitates the sorting of collections based on arbitrarily-deep sort 
 * criteria. It also has a special function, in that, it is also a java.util.Comparator. 
 * Consequently, CollationExpression can be used directly, after initialization, as a parameter 
 * to java.util.Collections.sort(List, Comparator). 
 * 
 * CollationExpression.evaluate() expects a java.util.List as its argument. If however, it is a
 * java.util.Collection, a new ArrayList is created from the collection and the resulting list
 * is sorted (and returned).  Thus, if the return value is not used, no change will result in 
 * the passed-in collection. 
 * 
 * @author Todd Fredrich
 * @since Aug 22, 2005
 * @version $Revision: 1.2 $
 */
public class CollationExpression<T>
implements Expression, Comparator<T>, Serializable
{
	// SECTION: CONSTANTS

	/**
     * Serializable ID.
     */
    private static final long serialVersionUID = 3944590239357646112L;

    
    // SECTION: VARIABLES
	
	private List<ComparingFunction> comparingFunctions;
	

	// SECTION: CONSTRUCTION
	
	/**
	 * Construct an empty CollationExpression. Caller must call addComparingFunction().
	 */
	public CollationExpression()
	{
		this.comparingFunctions = new ArrayList<ComparingFunction>();
	}
	
	/**
	 * Construct an CollationExpression for a single attribute. Caller may call addComparingFunction()
	 * to order by additional attributes.
	 *  
	 * @param attributeName the attribute name to use when ordering. Null not acceptable.
	 * @param collationOrder ascending or descending. Null not acceptable.
	 */
	public CollationExpression(String attributeName, CollationOrder collationOrder)
	{
		this(new ComparingFunction(new AccessorExpression(attributeName), collationOrder));
	}

	/**
	 * Construct an CollationExpression with a single comparing function. Caller may call addComparingFunction()
	 * to order by additional attributes.
	 * 
	 * @param comparingFunction a ComparingFunction to use when ordering. Null not acceptable.
	 */
	public CollationExpression(ComparingFunction comparingFunction)
	{
		this();
		comparingFunctions.add(comparingFunction);
	}

	/**
	 * Construct an CollationExpression with multiple comparing functions. Caller may call addComparingFunction()
	 * to order by additional attributes.
	 * 
	 * @param comparingFunctions a list of ComparingFunction. Null not acceptable.
	 */
	public CollationExpression(List<ComparingFunction> comparingFunctions)
	{
		super();
		this.comparingFunctions = new ArrayList<ComparingFunction>(comparingFunctions.size());
		this.comparingFunctions.addAll(comparingFunctions);
	}
	
	// PROTOCOL: ACCESSING
	
	/**
	 * Add a comparing function to the CollationExpression using the simple form of an attribute name
	 * and a sort order.
	 * 
	 * @param attributeName the attribute name to use when ordering. Null not acceptable.
	 * @param sortOrder ascending or descending. Null not acceptable.
	 * @return the CollationExpression to enable chaining addComparingFunction() calls on a single line.
	 */
	public CollationExpression<T> orderBy(String attributeName, CollationOrder sortOrder)
	{
		orderBy(new AccessorExpression(attributeName), sortOrder);
		return this;
	}
	
	/**
	 * Add a comparing function to the CollationExpression, passing in a unary function that returns a value.
	 * 
	 * @param expression a Function that returns a value, presumably of an attribute.
	 * @param collationOrder ascending or descending. Null not acceptable.
	 * @return the CollationExpression to enable chaining addComparingFunction() calls on a single line.
	 */
	public CollationExpression<T> orderBy(AccessorExpression expression, CollationOrder collationOrder)
	{
		comparingFunctions.add(new ComparingFunction(expression, collationOrder));
		return this;
	}

	// PROTOCOL: EVALUATE

	/**
	 * Evaluates the order-by expression on the argument. CollationExpression.evaluate() expects 
	 * a java.util.List as its argument. If however, it is a java.util.Collection, a new ArrayList 
	 * is created from the collection and the resulting list is sorted (and returned).
	 * 
	 * @param argument the list to order.
	 * @return the ordered collection.
	 * @throws EvaluationException if the argument is not a List or Collection.
	 */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object evaluate(Object argument)
	throws EvaluationException
	{
		Object results = argument;
		
		if (argument instanceof List)
		{
			Collections.sort((List) argument, this);
		}
		else if (argument instanceof Collection)
		{
			results = new ArrayList((Collection) argument);
			Collections.sort((List) results, this);
		}
		else
		{
			throw new EvaluationException("CollationExpression.evaluate() called to order a non-collection argument: " + argument.toString());
		}

		return results;
	}

	// PROTOCOL: COMPARATOR
	
	public int compare(T argument1, T argument2)
	{
		int result = 0;

		try
		{
			int intermediateResult = 0;

			for (int i = comparingFunctions.size(); i > 0; --i)
			{
				ComparingFunction comparingFunction = (ComparingFunction) comparingFunctions.get(i - 1);
				intermediateResult = ((Integer) comparingFunction.perform(argument1, argument2)).intValue();
				
				if (intermediateResult != 0) result = intermediateResult;
			}
		}
		catch(EvaluationException e)
		{
			//Logger.getLogger(Jbel.LOGGER_NAME).log(Level.SEVERE, "Unable to evaluate CollationExpression.compare()", e);
			e.printStackTrace();
		}

		return result;
	}
}
