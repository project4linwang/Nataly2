/* JayFX - A Fact Extractor Plug-in for Eclipse
 * Copyright (C) 2006  McGill University (http://www.cs.mcgill.ca/~swevo/jayfx)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * $Revision: 1.5 $
 */

package model;

import java.util.StringTokenizer;



/**
 * Represents a method element in the model.
 */
public class MethodElement extends AbstractElement
{
	/** Creates a method objects.  Such objects should not 
	 * be created directly but should be obtained through a
	 * FlyweightElementFactory.
	 * @param pId The unique descriptor of this method.  Comprises
	 * the fully qualified name of the declaring class, followed by
	 * the name of the method (or init for constructors), and the parameter
	 * list.
	 */
	private String returnType = null;
	protected MethodElement( String pId ,String rType )
	{
		super( pId );
		this.returnType=rType;
	}
	protected MethodElement(String pId){
		super( pId );
	}
	/** 
	 * Returns the category of this element type, i.e., a method.
	 * @return ICategories.METHOD
	 */
	public ICategories getCategory()
	{
		return ICategories.METHOD;
	}
	
	/**
	 * Equality for method elements is based on the equality
	 * of their corresponding ids.
	 * @param pObject the object to compare
	 * to.
	 * @return true if this object has the same 
	 * id as pObject.
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals( Object pObject )
	{
		if( !(pObject instanceof MethodElement))
			return false;
		else
			return getId().equals(((MethodElement)pObject).getId() );
	}
	
	/** 
	 * The hashcode is determined based on the id of the method.
	 * @return The hashcode of the id String for this method.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return getId().hashCode();
	}
	
	/**
	 * @return The name of the class declaring this method.
	 */
	public ClassElement getDeclaringClass()
	{
		String lName = getFirstParticle();
		int lIndex = lName.lastIndexOf( "." );
		ClassElement lReturn = null;
		lReturn =  (ClassElement)FlyweightElementFactory.getElement( ICategories.CLASS, lName.substring(0, lIndex));
		return lReturn;
	}
	
	/**
	 * @return The simple name of the method.
	 */
	public String getName()
	{
		String lName = getFirstParticle();
		int lIndex = lName.lastIndexOf( "." );
		return lName.substring(lIndex + 1, lName.length());
	}
	
	/**
	 * @return The String of parameter types for this method, 
	 * including the parentheses.
	 */
	public String getParameters()
	{
		int lIndex = getId().indexOf("(");
		return getId().substring(lIndex, getId().length());
	}
	
	/**
	 * @return Fully qualified name of the method.
	 */
	private String getFirstParticle()
	{
		int lIndex = getId().indexOf("(");
		return getId().substring(0, lIndex);
	}
	
	/** 
	 * @return The name of the package in which the declaring class of this
	 * method is defined in.
	 */
	public String getPackageName()
	{
		return getDeclaringClass().getPackageName();
	}
	
	/** 
	 * @return The id of this element without the package names for the 
	 * name of the method and the parameter types.
	 */
	public String getShortName()
	{
		String lReturn = getDeclaringClass().getShortName() + "." + getName() + "(";
		StringTokenizer lParser = new StringTokenizer( getParameters(), ",()" );
		int lNbTokens = lParser.countTokens();
		for( int i = 0 ; i < lNbTokens -1 ; i++ )
		{
			String lToken = lParser.nextToken();
			int lIndex = lToken.lastIndexOf( '.' );
			if( lIndex >= 0 )
			{
				lReturn += lToken.substring( lIndex + 1, lToken.length() ) + ",";
			}
			else
			{
				lReturn += lToken + ",";
			}
		}
		
		if( lNbTokens > 0 )
		{
			String lToken = lParser.nextToken();
			int lIndex = lToken.lastIndexOf( '.' );
			if( lIndex >= 0 )
			{
				lReturn += lToken.substring( lIndex + 1, lToken.length());
			}
			else
			{
				lReturn += lToken;
			}
		}
		return lReturn + ")";
	}
	//Modified by lin on 2011 Jan.10
	public String getReturnType(){
		if(returnType==null){
			returnType="void";
		}
		return returnType;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}

