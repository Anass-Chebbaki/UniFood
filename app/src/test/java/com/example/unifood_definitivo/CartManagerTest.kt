package com.example.unifood_definitivo

import com.example.unifood_definitivo.User.Model.Prodotti
import com.example.unifood_definitivo.User.View.Cart_List
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Questa è la classe di test unitari per la classe CartManager.
 * Qui si sta  testando la funzione `addToCart` per verificarne il giusto comportamento.
 */
class CartManagerTest {

    private lateinit var cartManager: Cart_List.CartManager

    @Before
    fun setup() {
        cartManager = Cart_List.CartManager
    }

    @Test
    fun testAddToCartSuccess() {
        val userId = "prova"
        val product = Prodotti("1", "Product 1", 10.0, "img_uri", "ingredients", "category", "img_uri2")
        val quantity = 2
        val imgUri = "img_uri"
        cartManager.addToCart(userId, product, quantity, imgUri)
        // Verifica che il carrello dell'utente contenga un elemento con le informazioni corrette
        val userCart = cartManager.userCarts[userId]
        assertNotNull(userCart)
        assertEquals(1, userCart?.size)
        val cartItem = userCart?.get(0)
        assertNotNull(cartItem)
        if (cartItem != null) {
            assertEquals(product, cartItem.product)
        }
        if (cartItem != null) {
            assertEquals(quantity, cartItem.quantity)
        }
        if (cartItem != null) {
            assertEquals(imgUri, cartItem.imgUri)
        }
        if (cartItem != null) {
            assertEquals(20.0, cartItem.total)
        } // Verifica che il totale sia calcolato correttamente
    }

}