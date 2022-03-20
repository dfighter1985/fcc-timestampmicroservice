package hu.dfighter1985.timestampmicroservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.dfighter1985.timestampmicroservice.controller.TimeStampResponse;

@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
public class TimeStampControllerTest
{
	private static final String BASE_URL = "/date";
	
	@Autowired
	private WebTestClient webTestClient;
	
	private void checkDateTimeResponse( final TimeStampResponse response ) throws Exception
	{
		assertFalse( response.unix.isEmpty() );
		assertFalse( response.dateString.isEmpty() );		
		assertTrue( response.unix.matches( "^[0-9]+$" ) );
		assertTrue( response.dateString.matches( "^[0-9]+\\-[0-9]+\\-[0-9]+ [0-9]+:[0-9]+:[0-9]+$" ) );
	}
	
	@Test
	public void testGetCurrentDateTime() throws Exception
	{
		final TimeStampResponse response =
				webTestClient
				.get()
				.uri( BASE_URL + "/" )
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(TimeStampResponse.class)
				.returnResult()
				.getResponseBody();
		
		checkDateTimeResponse( response );
		
		System.out.println( response.unix );
		System.out.println( response.dateString );
	}
	
	@Test
	public void testConvertUnixToUTC() throws Exception
	{
		final String unix = "123456789";
		
		final TimeStampResponse response =
				webTestClient
				.get()
				.uri( BASE_URL + "/" + unix )
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(TimeStampResponse.class)
				.returnResult()
				.getResponseBody();
		
		checkDateTimeResponse( response );
		
		assertEquals( unix, response.unix );
		assertEquals( "1970-01-02 11:17:36", response.dateString );
	}
	
	@Test
	public void testConvertUTCtoUnix() throws Exception
	{
		final String UTC = "1985-11-08 12:34:56";
		
		final TimeStampResponse response =
				webTestClient
				.get()
				.uri( BASE_URL + "/" + UTC )
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(TimeStampResponse.class)
				.returnResult()
				.getResponseBody();
		
		checkDateTimeResponse( response );
		
		assertEquals( "500297696000", response.unix );
		assertEquals( "1985-11-08 12:34:56", response.dateString );		
	}
}
