package com.ingenieriaweb.springboot.app.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ingenieriaweb.springboot.app.models.Usuario;
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

public class UsuarioPDF {
	private List<Usuario> listas;

	public UsuarioPDF(List<Usuario> listass) {
		super();
		this.listas = listass;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.BLUE);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("USERNAME", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("NOMBRES", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("EMAIL", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("ROL", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("ESTADO", fuente));
		tabla.addCell(celda);
	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Usuario usu : listas) {
			tabla.addCell(String.valueOf(usu.getId_usuario()));
			tabla.addCell(usu.getUsername());
			tabla.addCell(usu.getNombres());	
			tabla.addCell(usu.getEmail());	
			tabla.addCell(usu.getRol().getRol());
			if(usu.getEstado()) {
				tabla.addCell("HABILITADO");
			}else {
				tabla.addCell("DESHABILITADO");
			}
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.RED);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("LISTADO DE USUARIOS", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(6);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
