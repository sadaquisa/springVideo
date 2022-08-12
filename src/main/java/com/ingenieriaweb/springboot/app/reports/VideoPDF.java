package com.ingenieriaweb.springboot.app.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.VideoGenero;
import com.ingenieriaweb.springboot.app.models.VideoIdioma;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VideoPDF {
	
	private List<Video> listas;

	public VideoPDF(List<Video> listas) {
		super();
		this.listas = listas;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.BLUE);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("TITULO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("AÑO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("FORMATO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("GENERO", fuente));
		tabla.addCell(celda);		
		
		celda.setPhrase(new Phrase("IDIOMA", fuente));
		tabla.addCell(celda);		
		
		celda.setPhrase(new Phrase("PRECIO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("CANTIDAD", fuente));
		tabla.addCell(celda);	
		

		celda.setPhrase(new Phrase("ESTADO", fuente));
		tabla.addCell(celda);
		
	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		String genero="",idioma="";
		for (Video list : listas) {
			tabla.addCell(String.valueOf(list.getId_video()));
			tabla.addCell(list.getTitulo());
			tabla.addCell(list.getAnio());
			tabla.addCell(list.getFormato().getFormato());
			for(VideoGenero  itemGenero:   list.getList_video_genero()) {
				genero=genero+itemGenero.getGenero().getGenero()+",";
			}
			tabla.addCell(genero);
			for(VideoIdioma  itemIdioma:   list.getList_video_idioma()) {
				idioma=idioma+itemIdioma.getIdioma().getIdioma()+",";
			}
			tabla.addCell(idioma);
			tabla.addCell(String.valueOf(list.getPrecio()));
			tabla.addCell(String.valueOf(list.getCantidad()));
			
			if(list.isEstado()) {
				tabla.addCell("HABILITADO");
			}else {
				tabla.addCell("DESHABILITADO");
			}
			genero="";
			idioma="";
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLUE);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("LISTADO DE VIDEOS", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(9);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}


}
