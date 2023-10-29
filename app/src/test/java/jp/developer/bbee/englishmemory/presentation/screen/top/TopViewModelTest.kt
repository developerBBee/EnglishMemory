package jp.developer.bbee.englishmemory.presentation.screen.top

import com.google.common.truth.Truth.assertThat
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.SaveTranslateDataUseCase
import jp.developer.bbee.englishmemory.testdouble.FakeAccountService
import jp.developer.bbee.englishmemory.testdouble.FakeGetTranslateDataFromDbUseCase
import jp.developer.bbee.englishmemory.testdouble.FakeGetTranslateDataUseCase
import jp.developer.bbee.englishmemory.testdouble.FakeSaveTranslateDataUseCase
import jp.developer.bbee.englishmemory.testdouble.FakeTranslateRepository
import jp.developer.bbee.englishmemory.testdouble.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopViewModelTest {

    private lateinit var topViewModel: TopViewModel
    private lateinit var fakeService: FakeAccountService
    private lateinit var fakeRepository: FakeTranslateRepository
    private lateinit var fakeGetTranslateDataUseCase: FakeGetTranslateDataUseCase
    private lateinit var fakeGetTranslateDataFromDbUseCase: FakeGetTranslateDataFromDbUseCase
    private lateinit var fakeSaveTranslateDataUseCase: SaveTranslateDataUseCase

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        fakeService = FakeAccountService()
        fakeRepository = FakeTranslateRepository()
        fakeGetTranslateDataUseCase = FakeGetTranslateDataUseCase(fakeRepository)
        fakeGetTranslateDataFromDbUseCase = FakeGetTranslateDataFromDbUseCase(fakeRepository)
        fakeSaveTranslateDataUseCase = FakeSaveTranslateDataUseCase(fakeRepository)
    }

    private val testData = listOf(
        TranslateData(
            "english_test1",
            "wordType_test1",
            "translateToJapanese_test1",
            "importance_test1",
            "registrationDateUTC_test1",
        ),
        TranslateData(
            "english_test2",
            "wordType_test2",
            "translateToJapanese_test2",
            "importance_test2",
            "registrationDateUTC_test2",
        ),
    )

    @Test
    fun test_ViewModelInit_GetTranslateData() = runTest {
        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Not empty string is Success Token
        fakeService.setFakeToken("testToken")

        // Loading response test
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isFalse()

        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Not empty string is Success Token
        fakeService.setFakeToken("testToken")

        // Success response test
        fakeGetTranslateDataUseCase.setTestEmit(Async.Success(testData))
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        val actual2 = topViewModel.state.value
        val expect2 = TopState(translateData = testData)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isTrue()
    }

    @Test
    fun test_ViewModelInit_GetTranslateData_HasUser() = runTest {
        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Not empty string is Success Token
        fakeService.setFakeToken("testToken")

        // Loading response test
        fakeService.setHasUser(true)
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isFalse() // Not create if has user
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isFalse()

        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Not empty string is Success Token
        fakeService.setFakeToken("testToken")

        // Success response test
        fakeGetTranslateDataUseCase.setTestEmit(Async.Success(testData))
        fakeService.setHasUser(true)
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        val actual2 = topViewModel.state.value
        val expect2 = TopState(translateData = testData)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isFalse() // Not create if has user
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isTrue()
    }

    @Test
    fun test_ViewModelInit_TokenError() = runTest {
        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Empty string is Failure Token
        fakeService.setFakeToken("")

        // Loading response test
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        val actual1 = topViewModel.state.value
        val expect1 = TopState(error = "認証できませんでした。\nネットワーク接続を確認してください。")
        assertThat(actual1).isEqualTo(expect1)
    }

    @Test
    fun test_ViewModelInit_FailureTranslateData() = runTest {
        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Not empty string is Success Token
        fakeService.setFakeToken("testToken")

        // Failure response test
        val errorMassage = "error test"
        fakeGetTranslateDataUseCase.setTestEmit(Async.Failure(errorMassage))
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )

        val actual1 = topViewModel.state.value
        val expect1 = TopState(error = errorMassage)
        assertThat(actual1).isEqualTo(expect1)
    }

    @Test
    fun test_ViewModelInit_GetTranslateData_FromDb() = runTest {
        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Loading response test
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        topViewModel.load()
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isFalse()

        // Clear test result on service and repository
        fakeService.testClear()
        fakeRepository.testClear()

        // Success response test
        fakeGetTranslateDataFromDbUseCase.setTestEmit(Async.Success(testData))
        topViewModel = TopViewModel(
            fakeGetTranslateDataUseCase,
            fakeSaveTranslateDataUseCase,
            fakeGetTranslateDataFromDbUseCase,
            fakeService
        )
        topViewModel.load()
        val actual2 = topViewModel.state.value
        val expect2 = TopState(translateData = testData)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveCheck).isFalse()
    }
}