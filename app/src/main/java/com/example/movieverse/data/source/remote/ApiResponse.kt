package com.example.movieverse.data.source.remote

class ApiResponse<T>(val statusResponse: StatusResponse, val body: T, val message: String?) {

    companion object {
        fun <T> success(body: T): ApiResponse<T> = ApiResponse(StatusResponse.SUCCESS, body, null)

    }
}