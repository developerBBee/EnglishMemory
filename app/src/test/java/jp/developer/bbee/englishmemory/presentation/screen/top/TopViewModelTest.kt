package jp.developer.bbee.englishmemory.presentation.screen.top

import com.google.common.truth.Truth.assertThat
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.GetSavedLastDateUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataFromDbUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SaveTranslateDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SetSavedLastDateUseCase
import jp.developer.bbee.englishmemory.testdouble.FakeAccountService
import jp.developer.bbee.englishmemory.testdouble.FakePreferenceRepository
import jp.developer.bbee.englishmemory.testdouble.FakeTranslateRepository
import jp.developer.bbee.englishmemory.testdouble.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopViewModelTest {

    private lateinit var topViewModel: TopViewModel
    private lateinit var fakeService: FakeAccountService
    private lateinit var fakeRepository: FakeTranslateRepository
    private lateinit var fakePreference: FakePreferenceRepository
    private lateinit var getTranslateDataUseCase: GetTranslateDataUseCase
    private lateinit var getTranslateDataFromDbUseCase: GetTranslateDataFromDbUseCase
    private lateinit var saveTranslateDataUseCase: SaveTranslateDataUseCase
    private lateinit var setSavedLastDateUseCase: SetSavedLastDateUseCase
    private lateinit var getSavedLastDateUseCase: GetSavedLastDateUseCase

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        fakeService = FakeAccountService()
        fakeRepository = FakeTranslateRepository()
        fakePreference = FakePreferenceRepository()

        val dispatcher = mainCoroutineRule.testDispatcher
        getTranslateDataUseCase = GetTranslateDataUseCase(dispatcher, fakeRepository)
        getTranslateDataFromDbUseCase = GetTranslateDataFromDbUseCase(dispatcher, fakeRepository)
        saveTranslateDataUseCase = SaveTranslateDataUseCase(dispatcher, fakeRepository)
        setSavedLastDateUseCase = SetSavedLastDateUseCase(dispatcher, fakePreference)
        getSavedLastDateUseCase = GetSavedLastDateUseCase(fakePreference)
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
    fun test_TopViewModel_Init_Load_Save_Reset() = runTest {
        /// Set fake parameter
        fakeService.fakeToken = "testToken"
        fakeService.delayTime = 100
        fakeRepository.testGetResult = testData
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService,
        )

        advanceTimeBy(100)

        // Check CreateAnonymous
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue() // updated
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check token
        val actual2 = topViewModel.state.value
        val expect2 = TopState(isLoading = true)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue() // updated
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check GetTranslateData
        val actual3 = topViewModel.state.value
        val expect3 = TopState(translateData = testData) // updated
        assertThat(actual3).isEqualTo(expect3)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check SaveTranslateData
        val actual4 = topViewModel.state.value
        val expect4 = TopState(translateData = testData)
        assertThat(actual4).isEqualTo(expect4)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(testData) // updated
        assertThat(topViewModel.isReady).isTrue()
        assertThat(topViewModel.isRestart).isFalse()

        // Load from DB test
        topViewModel.load()

        advanceTimeBy(100)

        // Check loading state
        val actual5 = topViewModel.state.value
        val expect5 = TopState(isLoading = true)
        assertThat(actual5).isEqualTo(expect5)

        advanceTimeBy(100)

        // Check GetTranslateDataFromDb
        val actual6 = topViewModel.state.value
        val expect6 = TopState(translateData = testData)
        assertThat(actual6).isEqualTo(expect6)

        // Save to DB test
        fakeRepository.testSaveData = null // Reset check data
        topViewModel.save()

        advanceTimeBy(100)

        // Check SaveTranslateData - progress saving
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check SaveTranslateData - completed
        assertThat(fakeRepository.testSaveData).isEqualTo(testData)

        // Check restart
        assertThat(topViewModel.isReady).isTrue()
        assertThat(topViewModel.isRestart).isFalse()
        topViewModel.restart()
        assertThat(topViewModel.isReady).isFalse()
        assertThat(topViewModel.isRestart).isTrue()
        assertThat(topViewModel.state.value).isEqualTo(TopState(isLoading = true))
    }

    @Test
    fun test_TopViewModelInitHasUser() = runTest {
        // Set fake parameter
        fakeService.testHasUser = true
        fakeService.fakeToken = "testToken"
        fakeService.delayTime = 100
        fakeRepository.testGetResult = testData
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService
        )

        advanceTimeBy(100)

        // Check CreateAnonymous - Not create if has user
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isFalse() // Not create if has user
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check token
        val actual2 = topViewModel.state.value
        val expect2 = TopState(isLoading = true)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isFalse()
        assertThat(fakeService.userTokenCheck).isTrue() // updated
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check GetTranslateData
        val actual3 = topViewModel.state.value
        val expect3 = TopState(translateData = testData) // updated
        assertThat(actual3).isEqualTo(expect3)
        assertThat(fakeService.createAnonymousCheck).isFalse()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check SaveTranslateData
        val actual4 = topViewModel.state.value
        val expect4 = TopState(translateData = testData)
        assertThat(actual4).isEqualTo(expect4)
        assertThat(fakeService.createAnonymousCheck).isFalse()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(testData) // updated
    }

    @Test
    fun test_TopViewModelInitTokenError() = runTest {
        // Set fake parameter
        fakeService.fakeToken = ""
        fakeService.delayTime = 100
        val errorMassage = "Token Failure in Test"
        fakeService.failureMessage = errorMassage
        fakeRepository.testGetResult = testData
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService
        )

        advanceTimeBy(100)

        // Check CreateAnonymous
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue() // updated
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check token - Failure
        val actual2 = topViewModel.state.value
        val expect2 = TopState(error = errorMassage)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isFalse() // failure
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // No change on failure
        val actual3 = topViewModel.state.value
        val expect3 = TopState(error = errorMassage)
        assertThat(actual3).isEqualTo(expect3)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)
    }

    @Test
    fun test_TopViewModelInitGetTranslateDataByTokenFailure() = runTest {
        // Set fake parameter
        fakeService.fakeToken = "testToken"
        fakeService.delayTime = 100
        fakeRepository.failureGetTranslateDataByToken = true
        val errorMassage = "Test for GetTranslateDataByToken Failure"
        fakeRepository.failureMessage = errorMassage
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService
        )

        advanceTimeBy(100)

        // Check CreateAnonymous
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue() // updated
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check token
        val actual2 = topViewModel.state.value
        val expect2 = TopState(isLoading = true)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue() // updated
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check GetTranslateData
        val actual3 = topViewModel.state.value
        val expect3 = TopState(error = errorMassage) // updated
        assertThat(actual3).isEqualTo(expect3)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // No change on failure
        val actual4 = topViewModel.state.value
        val expect4 = TopState(error = errorMassage)
        assertThat(actual4).isEqualTo(expect4)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)
    }

    @Test
    fun test_TopViewModelInitSaveTranslateDataFailure() = runTest {
        // Set fake parameter
        fakeService.fakeToken = "testToken"
        fakeService.delayTime = 100
        fakeRepository.failureSaveTranslateData = true
        fakeRepository.testGetResult = testData
        val errorMassage = "Test for SaveTranslateData Failure"
        fakeRepository.failureMessage = errorMassage
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService
        )

        advanceTimeBy(100)

        // Check CreateAnonymous
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)
        assertThat(fakeService.createAnonymousCheck).isTrue() // updated
        assertThat(fakeService.userTokenCheck).isFalse()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check token
        val actual2 = topViewModel.state.value
        val expect2 = TopState(isLoading = true)
        assertThat(actual2).isEqualTo(expect2)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue() // updated
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check GetTranslateData
        val actual3 = topViewModel.state.value
        val expect3 = TopState(translateData = testData) // updated
        assertThat(actual3).isEqualTo(expect3)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // Check SaveTranslateData
        val actual4 = topViewModel.state.value
        val expect4 = TopState(error = errorMassage) // updated
        assertThat(actual4).isEqualTo(expect4)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)

        advanceTimeBy(100)

        // No change on failure
        val actual5 = topViewModel.state.value
        val expect5 = TopState(error = errorMassage)
        assertThat(actual5).isEqualTo(expect5)
        assertThat(fakeService.createAnonymousCheck).isTrue()
        assertThat(fakeService.userTokenCheck).isTrue()
        assertThat(fakeRepository.testSaveData).isEqualTo(null)
    }
    @Test
    fun test_TopViewModelLoadFailure() = runTest {
        // Set fake parameter
        fakeService.fakeToken = "testToken"
        fakeService.delayTime = 100
        fakeRepository.failureGetTranslateData = true
        val errorMassage = "Test for GetTranslateDataFromDb Failure"
        fakeRepository.failureMessage = errorMassage
        fakeRepository.delayTime = 100

        // ViewModel init test
        topViewModel = TopViewModel(
            getTranslateDataUseCase,
            saveTranslateDataUseCase,
            getTranslateDataFromDbUseCase,
            setSavedLastDateUseCase,
            getSavedLastDateUseCase,
            fakeService
        )

        // Passed ViewModel init process
        advanceTimeBy(500)

        // Load from DB test
        topViewModel.load()

        advanceTimeBy(100)

        // Check loading state
        val actual1 = topViewModel.state.value
        val expect1 = TopState(isLoading = true)
        assertThat(actual1).isEqualTo(expect1)

        advanceTimeBy(100)

        // Check GetTranslateDataFromDb - Failure
        val actual2 = topViewModel.state.value
        val expect2 = TopState(error = errorMassage)
        assertThat(actual2).isEqualTo(expect2)
    }
}