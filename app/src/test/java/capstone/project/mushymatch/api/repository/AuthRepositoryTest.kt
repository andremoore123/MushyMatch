package capstone.project.mushymatch.api.repository

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: IAuthRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun loginWithCorrectCredentials() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val expectedResponse = AuthResponse(isError = false, message = "Login successful")
        whenever(authRepository.login(email,password, {})).thenAnswer {
            val completion = it.getArgument<((callback: AuthResponse) -> Unit)>(1)
            completion.invoke(expectedResponse)
        }

        authRepository.login(email, password) {
            assertEquals(expectedResponse, it)
        }
    }

    @Test
    fun loginWithIncorrectCredentials() {
        // Given
        val email = "test@example.com"
        val password = "incorrectPassword"
        val expectedResponse = AuthResponse(isError = true, message = "Invalid credentials")

        // Use the ArgumentCaptor to capture the callback passed to the login function
        whenever(authRepository.login(email, password, {})).thenAnswer {
            // Get the callback from the captured arguments
            val completion = it.getArgument<((AuthResponse) -> Unit)>(2)
            completion.invoke(expectedResponse)
        }

        // When
        authRepository.login(email, password) { actualResponse ->
            // Then
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun registerWithNewEmail() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val expectedResponse = AuthResponse(isError = false, message = "Registration successful")
        whenever(authRepository.register(email,password, {})).thenAnswer {
            val completion = it.getArgument<((callback: AuthResponse) -> Unit)>(1)
            completion.invoke(expectedResponse)
        }

        authRepository.register(email, password) {
            assertEquals(expectedResponse, it)
        }
    }

    @Test
    fun registerWithUsedEmail() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val expectedResponse = AuthResponse(isError = true, message = "Email Used")
        whenever(authRepository.register(email,password, {})).thenAnswer {
            val completion = it.getArgument<((callback: AuthResponse) -> Unit)>(1)
            completion.invoke(expectedResponse)
        }

        authRepository.register(email, password) {
            assertEquals(expectedResponse, it)
        }
    }
}
