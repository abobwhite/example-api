package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

/**
 * Tests 
 */
// UTF-8 URL Encodings: http://www.utf8-chartable.de/
public class SitemapUrlTest {

	@Test
	public void encodeSitemapUrlWithDoubleQuotedNameForProducts() {
		SitemapUrl sitemapUrl = SitemapUrl.createProductUrl(500001101L, "Tequila \"Rancho Grande Reposado\"", new Date(), false);
		assertTrue(sitemapUrl.getLocation().contains("%22"));
	}
	
	@Test
	public void encodeSitemapUrlWithSingleQuotedNameForProducts() {
		SitemapUrl sitemapUrl = SitemapUrl.createProductUrl(500000863L, "Nero D'avola", new Date(), false);
		assertTrue(sitemapUrl.getLocation().contains("%27"));
	}
	
	@Test
	public void encodeSitemapUrlWithSingleAndDoubleQuotedNameForProducts() {
		SitemapUrl sitemapUrl = SitemapUrl.createProductUrl(500001001L, "Red Wine \"Barbera d'Alba\"", new Date(), false);
		assertTrue(sitemapUrl.getLocation().contains("%22"));
		assertTrue(sitemapUrl.getLocation().contains("%27"));
	}
	
	@Test
	public void encodeSitemapUrlWithCommaNameForProducts() {
		SitemapUrl sitemapUrl = SitemapUrl.createProductUrl(500000965L, "Cocoa Bean, Shell, Powder", new Date(), false);
		assertTrue(sitemapUrl.getLocation().contains("%2C"));
	}
	
	@Test
	public void encodeSitemapUrlWithColonNameForProducts() {
		SitemapUrl sitemapUrl = SitemapUrl.createProductUrl(500002036L, "Pre-Columbian Painting: The Mochicano and Nazca Lines", new Date(), false);
		assertTrue(sitemapUrl.getLocation().contains("%3A"));
	}

}
