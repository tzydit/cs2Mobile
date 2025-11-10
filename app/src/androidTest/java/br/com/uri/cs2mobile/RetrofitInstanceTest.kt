package br.com.uri.cs2mobile.network

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

class RetrofitInstanceTest {

    @Test
    fun testConexaoApiSkins() = runBlocking {
        val response = RetrofitInstance.api.getSkins("en")

        assertNotNull("A resposta da API não deve ser nula", response)
        assertTrue("A lista de skins não deve estar vazia", response.isNotEmpty())

        println("Conexão bem-sucedida! ${response.size} skins carregadas da API.")
    }
}
