package com.ingenieriaweb.springboot.app.reports;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ingenieriaweb.springboot.app.models.Video;
import com.ingenieriaweb.springboot.app.models.VideoGenero;
import com.ingenieriaweb.springboot.app.models.VideoIdioma;

public class VideoEXCEL {
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Video> listas;

	public VideoEXCEL(List<Video> listas) {
		this.listas = listas;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Idiomas");
	}

	private void escribirCabeceraDeTabla() {
		Row fila = hoja.createRow(0);
		
		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);
		fuente.setFontHeight(16);
		estilo.setFont(fuente);
		
		Cell celda = fila.createCell(0);
		celda.setCellValue("ID");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(1);
		celda.setCellValue("TITULO");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(2);
		celda.setCellValue("AÑO");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(3);
		celda.setCellValue("FORMATO");
		celda.setCellStyle(estilo);		

		celda = fila.createCell(4);
		celda.setCellValue("GENERO");
		celda.setCellStyle(estilo);		

		celda = fila.createCell(5);
		celda.setCellValue("IDIOMA");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(6);
		celda.setCellValue("PRECIO");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(7);
		celda.setCellValue("CANTIDAD");
		celda.setCellStyle(estilo);		
		
		celda = fila.createCell(8);
		celda.setCellValue("ESTADO");
		celda.setCellStyle(estilo);
	}
		

	
	private void escribirDatosDeLaTabla() {
		int nueroFilas = 1;
		String genero="",idioma="";

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);
		
		for(Video tab : listas) {
			Row fila = hoja.createRow(nueroFilas ++);
			
			Cell celda = fila.createCell(0);
			celda.setCellValue(tab.getId_video());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(1);
			celda.setCellValue(tab.getTitulo());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(2);
			celda.setCellValue(tab.getAnio());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(3);
			celda.setCellValue(tab.getFormato().getFormato());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);
			
			for(VideoGenero  itemGenero:   tab.getList_video_genero()) {
				genero=genero+itemGenero.getGenero().getGenero()+",";
			}
			celda = fila.createCell(4);
			celda.setCellValue(genero);
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);
			
			for(VideoIdioma  itemIdioma:   tab.getList_video_idioma()) {
				idioma=idioma+itemIdioma.getIdioma().getIdioma()+",";
			}
			celda = fila.createCell(5);
			celda.setCellValue(idioma);
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(6);
			celda.setCellValue(tab.getPrecio());
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(7);
			celda.setCellValue(tab.getCantidad());
			hoja.autoSizeColumn(7);
			celda.setCellStyle(estilo);	
			
			if(tab.isEstado()) {
				celda = fila.createCell(8);
				celda.setCellValue("HABILITADO");
				hoja.autoSizeColumn(8);
				celda.setCellStyle(estilo);
			}else {
				celda = fila.createCell(8);
				celda.setCellValue("DESHABILITADO");
				hoja.autoSizeColumn(8);
				celda.setCellStyle(estilo);
			}
			genero="";
			idioma="";
		}
	}
	
	public void exportar(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTabla();
		escribirDatosDeLaTabla();
		
		ServletOutputStream outPutStream = response.getOutputStream();
		libro.write(outPutStream);
		
		libro.close();
		outPutStream.close();
	}


}
