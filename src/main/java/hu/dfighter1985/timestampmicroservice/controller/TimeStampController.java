package hu.dfighter1985.timestampmicroservice.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class Utils
{
	public static TimeStampResponse buildResponse( final Date date )
	{
		final SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		final TimeStampResponse response = new TimeStampResponse();
		response.unix = Long.toString( date.getTime() );
		response.dateString = format.format( date );
		return response;
	}
}

@RestController
@RequestMapping("/date")
public class TimeStampController
{
	@GetMapping("/")
	public ResponseEntity< TimeStampResponse > getDateTime()
	{
		final Date date = new Date();
		final TimeStampResponse response = Utils.buildResponse( date );
		
		return ResponseEntity.ok( response );
	}
	
	@GetMapping("/{inputDate}")
	public ResponseEntity convertDate( @PathVariable("inputDate") final String inputDate )
	{
		ResponseEntity response = null;
		
		if( inputDate.matches( "^[0-9]+$" ) )
		{
			final Date date = new Date( Long.parseLong( inputDate ) );
			response = ResponseEntity.ok( Utils.buildResponse( date ) );
		}
		else
		{
			try
			{
				final SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				final Date date = format.parse( inputDate );
				response = ResponseEntity.ok( Utils.buildResponse( date ) );
			}
			catch( final ParseException pe )
			{
				response = ResponseEntity.badRequest().body( new ErrorResponse("Invalid date provided") );
			}
		}
		
		return response;
	}
}
