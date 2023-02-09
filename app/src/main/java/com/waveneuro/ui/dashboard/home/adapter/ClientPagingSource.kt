//package com.waveneuro.ui.dashboard.home.adapter
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem
//import retrofit2.HttpException
//import java.io.IOException
//
//internal class ClientPagingSource(
//    private val unsplashApi: UnsplashApi,
//    private val query: String
//) : PagingSource<Int, PatientItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PatientItem> {
//        val position = params.key ?: CLIENT_STARTING_PAGE_INDEX
//
//        return try {
//            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
//            val photos = response.results
//            total = response.total
//
//            LoadResult.Page(
//                data = photos,
//                prevKey = if (position == CLIENT_STARTING_PAGE_INDEX) null else position - 1,
//                nextKey = if (photos.isEmpty()) null else position + 1
//            )
//        } catch (exception: IOException) {
//            LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, PatientItem>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val page = state.closestPageToPosition(anchorPosition) ?: return null
//        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
//    }
//
//    companion object{
//        private const val CLIENT_STARTING_PAGE_INDEX = 1
//        var total: String? = ""
//    }
//
//}