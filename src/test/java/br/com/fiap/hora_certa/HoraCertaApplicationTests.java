package br.com.fiap.hora_certa;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Percorre o CRUD das quatro entidades conferindo os códigos HTTP. */
@SpringBootTest
@AutoConfigureMockMvc
class HoraCertaApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void crudCompletoComCodigosHttp() throws Exception {
		// ---------- Cuidadores ----------
		String cuidador = """
				{"nome":"Carlos Souza","email":"carlos@email.com","telefone":"11987654321","parentesco":"Filho"}""";
		long cuidadorId = id(enviar(post("/api/v1/cuidadores"), cuidador)
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/api/v1/cuidadores/")))
				.andExpect(jsonPath("$.dataCadastro").exists()));

		enviar(post("/api/v1/cuidadores"), cuidador)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensagem", containsString("e-mail")));

		enviar(post("/api/v1/cuidadores"), "{\"nome\":\"\",\"email\":\"invalido\",\"telefone\":\"12\"}")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos[*].campo", hasItem("email")))
				.andExpect(jsonPath("$.campos[*].campo", hasItem("telefone")));

		mvc.perform(get("/api/v1/cuidadores")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome").value("Carlos Souza"));
		mvc.perform(get("/api/v1/cuidadores/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(content().string(not(containsString("trace"))));

		enviar(put("/api/v1/cuidadores/" + cuidadorId), cuidador.replace("Filho", "Neto"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.parentesco").value("Neto"));

		// ---------- Idosos ----------
		enviar(post("/api/v1/idosos"), idoso(999)).andExpect(status().isNotFound());
		long idosoId = id(enviar(post("/api/v1/idosos"), idoso(cuidadorId))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.toleranciaMinutos").value(30))
				.andExpect(jsonPath("$.cuidadorNome").value("Carlos Souza")));

		// ---------- Medicamentos ----------
		long medicamentoId = id(enviar(post("/api/v1/medicamentos"), """
				{"nome":"Losartana","dosagem":"50 mg","horario":"08:00","idosoId":%d}""".formatted(idosoId))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.horario").value("08:00"))
				.andExpect(jsonPath("$.ativo").value(true))
				.andExpect(jsonPath("$.lembretePorVoz").value(true))
				.andExpect(jsonPath("$.idosoNome").value("Maria Souza")));

		enviar(put("/api/v1/medicamentos/" + medicamentoId), """
				{"nome":"Losartana","dosagem":"100 mg","horario":"09:30","lembretePorVoz":true,"ativo":true}""")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dosagem").value("100 mg"))
				.andExpect(jsonPath("$.horario").value("09:30"));

		enviar(post("/api/v1/medicamentos"), "{\"nome\":\"X\",\"horario\":\"25:99\",\"idosoId\":1}")
				.andExpect(status().isBadRequest());

		// ---------- Doses ----------
		long doseId = id(enviar(post("/api/v1/doses"), """
				{"dataHoraPrevista":"2026-11-16T08:00:00","medicamentoId":%d}""".formatted(medicamentoId))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("PENDENTE"))
				.andExpect(jsonPath("$.cuidadorAvisado").value(false))
				.andExpect(jsonPath("$.medicamentoNome").value("Losartana")));

		enviar(put("/api/v1/doses/" + doseId), """
				{"dataHoraPrevista":"2026-11-16T08:00:00","status":"NAO_CONFIRMADA","cuidadorAvisado":true}""")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("NAO_CONFIRMADA"))
				.andExpect(jsonPath("$.cuidadorAvisado").value(true));

		enviar(put("/api/v1/doses/" + doseId), """
				{"dataHoraPrevista":"2026-11-16T08:00:00","dataHoraConfirmacao":"2026-11-16T08:40:00",
				 "status":"PENDENTE","cuidadorAvisado":true}""")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("TOMADA"));

		enviar(post("/api/v1/doses"), """
				{"dataHoraPrevista":"2026-11-16T20:00:00","dataHoraConfirmacao":"2026-11-16T08:00:00",
				 "medicamentoId":%d}""".formatted(medicamentoId))
				.andExpect(status().isConflict());

		// ---------- Exclusões e integridade ----------
		mvc.perform(delete("/api/v1/cuidadores/" + cuidadorId)).andExpect(status().isConflict());
		mvc.perform(delete("/api/v1/medicamentos/" + medicamentoId)).andExpect(status().isConflict());

		mvc.perform(delete("/api/v1/doses/" + doseId)).andExpect(status().isNoContent());
		mvc.perform(get("/api/v1/doses/" + doseId)).andExpect(status().isNotFound());
		mvc.perform(delete("/api/v1/medicamentos/" + medicamentoId)).andExpect(status().isNoContent());
		mvc.perform(delete("/api/v1/idosos/" + idosoId)).andExpect(status().isNoContent());
		mvc.perform(delete("/api/v1/cuidadores/" + cuidadorId)).andExpect(status().isNoContent());
		mvc.perform(delete("/api/v1/cuidadores/" + cuidadorId)).andExpect(status().isNotFound());

		mvc.perform(get("/api/v1/cuidadores/abc")).andExpect(status().isBadRequest());
		enviar(post("/api/v1/cuidadores"), "{json quebrado").andExpect(status().isBadRequest());
	}

	@Test
	void raizAbreSwaggerEDocumentacaoExiste() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", containsString("swagger-ui")));
		mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("Hora Certa API"))
				.andExpect(jsonPath("$.paths['/api/v1/cuidadores/{id}']").exists())
				.andExpect(jsonPath("$.paths['/api/v1/doses']").exists());
	}

	private String idoso(long cuidadorId) {
		return """
				{"nome":"Maria Souza","dataNascimento":"1948-03-15","telefone":"11912345678",
				 "observacoes":"Hipertensa e diabética","cuidadorId":%d}""".formatted(cuidadorId);
	}

	private ResultActions enviar(MockHttpServletRequestBuilder req, String corpo) throws Exception {
		return mvc.perform(req.contentType(MediaType.APPLICATION_JSON).content(corpo));
	}

	private long id(ResultActions acao) throws Exception {
		Number id = JsonPath.read(acao.andReturn().getResponse().getContentAsString(), "$.id");
		return id.longValue();
	}
}
