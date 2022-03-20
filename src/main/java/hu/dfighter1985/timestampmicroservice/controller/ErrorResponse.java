package hu.dfighter1985.timestampmicroservice.controller;

class ErrorResponse
{
	ErrorResponse( final String error )
	{
		this.error = error;
	}
	
	public String error;
}