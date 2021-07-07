package pe.edu.utp.electroplus.controller;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class ReporteController {
    
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/jasper/repventas")
    public void generateReporteVentas(HttpServletResponse response)  {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"ventas.pdf\""));
        try {
            OutputStream out = response.getOutputStream();
            Resource resource = resourceLoader.getResource("classpath:./reporte/ReporteDeVentas.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, 
                    Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection());
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
