package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.DictionaryController.Source;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

	public enum Source {
		WordNet(new WordNetSourceAdapter());
		private SourceAdapter sourceAdapter;

		Source(SourceAdapter sourceAdapter) {
			this.sourceAdapter = sourceAdapter;
		}

		public SourceAdapter getSourceAdapter() {
			return sourceAdapter;
		}

	}

	@CrossOrigin()
	@RequestMapping(value = "/define/{source}/{word}", method = RequestMethod.GET)
	public WordDefinitionDTO define(@PathVariable("source") String source, @PathVariable("word") String word) {
		try {
			Source source2 = Source.valueOf(source);
			return source2.getSourceAdapter().define(word);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class WordDefinitionDTO {
		private List<String> nouns;
		private List<String> verbs;
		private String endpoint;
		private String innerHtml;

		public List<String> getNouns() {
			return nouns;
		}

		public void setNouns(List<String> noun) {
			this.nouns = noun;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public List<String> getVerbs() {
			return verbs;
		}

		public void setVerbs(List<String> verbs) {
			this.verbs = verbs;
		}

		public String getInnerHtml() {
			return innerHtml;
		}

		public void setInnerHtml(String innerHtml) {
			this.innerHtml = innerHtml;
		}
	}

	private static abstract class SourceAdapter extends HTMLEditorKit {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3778680855933629315L;

		public abstract WordDefinitionDTO define(String word);

		protected HTMLDocument getDocument(String endpoint) {
			try {
				URL u = new URL(endpoint);
				BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
				HTMLDocument doc = (HTMLDocument) createDefaultDocument();
				getParser().parse(br, doc.getReader(0), true);
				if (br != null) {
					br.close();
				}
				return doc;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private static class WordNetSourceAdapter extends SourceAdapter {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1867430872009109376L;
		private static final String ENDPOINT = "http://wordnetweb.princeton.edu/perl/webwn?s=";

		@Override
		public WordDefinitionDTO define(String word) {
			WordDefinitionDTO def = new WordDefinitionDTO();
			String endpoint = ENDPOINT + word;
			def.setEndpoint(endpoint);
			HTMLDocument doc = getDocument(endpoint);
			for (ElementIterator iterator = new ElementIterator(doc); iterator.next() != null;) {
				Element el = iterator.current();
				String name = el.getName();
				System.out.println(name);
			}
			return def;
		}
	}

}
