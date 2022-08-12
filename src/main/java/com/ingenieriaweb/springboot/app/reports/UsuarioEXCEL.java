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

import com.ingenieriaweb.springboot.app.models.Rol;
import com.ingenieriaweb.springboot.app.models.Usuario;

public class UsuarioEXCEL {
	
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Usuario> listas;

	public UsuarioEXCEL(List<Usuario> listass) {
		this.listas = listass;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Usuarios");
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
		celda.setCellValue("USERNAME");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(2);
		celda.setCellValue("NOMBRES");
		celda.setCellStyle(estilo);
		
		
		celda = fila.createCell(3);
		celda.setCellValue("EMAIL");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(4);
		celda.setCellValue("ROL");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(5);
		celda.setCellValue("ESTADO");
		celda.setCellStyle(estilo);
	}
		

	
	private void escribirDatosDeLaTabla() {
		int nueroFilas = 1;
		
		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);
		
		for(Usuario tab : listas) {
			Row fila = hoja.createRow(nueroFilas ++);
			
			Cell celda = fila.createCell(0);
			celda.setCellValue(tab.getId_usuario());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(1);
			celda.setCellValue(tab.getUsername());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(2);
			celda.setCellValue(tab.getNombres());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);	
			
			
			celda = fila.createCell(3);
			celda.setCellValue(tab.getEmail());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);	
			
			
			celda = fila.createCell(4);
			celda.setCellValue(tab.getRol().getRol());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);	
			
			if(tab.getEstado()) {
				celda = fila.createCell(5);
				celda.setCellValue("HABILITADO");
				hoja.autoSizeColumn(5);
				celda.setCellStyle(estilo);
			}else {
				celda = fila.createCell(5);
				celda.setCellValue("DESHABILITADO");
				hoja.autoSizeColumn(5);
				celda.setCellStyle(estilo);
			}
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
