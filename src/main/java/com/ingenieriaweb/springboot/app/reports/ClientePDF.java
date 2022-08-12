package com.ingenieriaweb.springboot.app.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ingenieriaweb.springboot.app.models.Cliente;
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

public class ClientePDF {
	
	private List<Cliente> listas;

	public ClientePDF(List<Cliente> listas) {
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

		celda.setPhrase(new Phrase("DNI", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("NOMBRES", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("EDAD", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("GENERO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("TELEFONO", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("URBANIZACION", fuente));
		tabla.addCell(celda);		

		celda.setPhrase(new Phrase("ESTADO", fuente));
		tabla.addCell(celda);
		
	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Cliente list : listas) {
			tabla.addCell(String.valueOf(list.getId_cliente()));
			tabla.addCell(list.getDni());
			tabla.addCell(list.getNombres());
			tabla.addCell(String.valueOf(list.getEdad()));
			tabla.addCell(list.getGenero());
			tabla.addCell(list.getTelefono());
			tabla.addCell(list.getUrbanizacion().getUrbanizacion());
			if(list.isEstado()) {
				tabla.addCell("HABILITADO");
			}else {
				tabla.addCell("DESHABILITADO");
			}
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLUE);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("LISTADO DE CLIENTES", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(8);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}

}
