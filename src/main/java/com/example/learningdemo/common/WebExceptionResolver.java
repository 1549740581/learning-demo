package com.example.learningdemo.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.learningdemo.util.RequestIdHolder;
import com.example.learningdemo.util.Json2;
import com.google.common.collect.Maps;

public class WebExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Log category to use when no mapped handler is found for a request.
     *
     * @see #pageNotFoundLogger
     */
    private static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";

    /**
     * Additional logger to use when no mapped handler is found for a request.
     *
     * @see #PAGE_NOT_FOUND_LOG_CATEGORY
     */
    private static final Log pageNotFoundLogger = LogFactory.getLog(PAGE_NOT_FOUND_LOG_CATEGORY);

    public HttpResponse doResolveException(HttpServletRequest request, Exception ex) {
        HttpResponse response = new HttpResponse();
        try {
            if (ex instanceof HttpRequestMethodNotSupportedException) {
                return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request,
                        response);
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response
                );
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response
                );
            } else if (ex instanceof MissingPathVariableException) {
                return handleMissingPathVariable((MissingPathVariableException) ex, request,
                        response);
            } else if (ex instanceof MissingServletRequestParameterException) {
                return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request,
                        response);
            } else if (ex instanceof ServletRequestBindingException) {
                return handleServletRequestBindingException((ServletRequestBindingException) ex, request, response
                );
            } else if (ex instanceof ConversionNotSupportedException) {
                return handleConversionNotSupported((ConversionNotSupportedException) ex, request, response);
            } else if (ex instanceof TypeMismatchException) {
                return handleTypeMismatch((TypeMismatchException) ex, request, response);
            } else if (ex instanceof HttpMessageNotReadableException) {
                return handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response);
            } else if (ex instanceof HttpMessageNotWritableException) {
                return handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response);
            } else if (ex instanceof MethodArgumentNotValidException) {
                return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response);
            } else if (ex instanceof MissingServletRequestPartException) {
                return handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request,
                        response);
            } else if (ex instanceof BindException) {
                return handleBindException((BindException) ex, request, response);
            } else if (ex instanceof NoHandlerFoundException) {
                return handleNoHandlerFoundException((NoHandlerFoundException) ex, request, response);
            }
        } catch (Exception handlerException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            }
        }
        return new HttpResponse(500, "未知错误");
    }

    /**
     * Handle the case where no request handler method was found for the particular HTTP request method.
     * <p>The default implementation logs a warning, sends an HTTP 405 error, sets the "Allow" header,
     * and returns an empty {@code ModelAndView}. Alternatively, a fallback view could be chosen,
     * or the HttpRequestMethodNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpRequestMethodNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     *                 at the time of the exception (for example, if multipart resolution failed)
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                             HttpServletRequest request, HttpResponse response) {

        pageNotFoundLogger.warn(ex.getMessage());
        String[] supportedMethods = ex.getSupportedMethods();
        if (supportedMethods != null) {
            response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
        }
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ex.getMessage());
        return response;
    }

    /**
     * Handle the case where no {@linkplain org.springframework.http.converter.HttpMessageConverter message converters}
     * were found for the PUT or POSTed content.
     * <p>The default implementation sends an HTTP 415 error, sets the "Accept" header,
     * and returns an empty {@code ModelAndView}. Alternatively, a fallback view could
     * be chosen, or the HttpMediaTypeNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpMediaTypeNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                         HttpServletRequest request, HttpResponse response) {

        response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            response.setHeader("Accept", MediaType.toString(mediaTypes));
        }
        return response;
    }

    /**
     * Handle the case where no {@linkplain org.springframework.http.converter.HttpMessageConverter message converters}
     * were found that were acceptable for the client (expressed via the {@code Accept} header.
     * <p>The default implementation sends an HTTP 406 error and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the HttpMediaTypeNotAcceptableException
     * could be rethrown as-is.
     *
     * @param ex       the HttpMediaTypeNotAcceptableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                          HttpServletRequest request, HttpResponse response) {

        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return response;
    }

    /**
     * Handle the case when a declared path variable does not match any extracted URI variable.
     * <p>The default implementation sends an HTTP 500 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the MissingPathVariableException
     * could be rethrown as-is.
     *
     * @param ex       the MissingPathVariableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     * @since 4.2
     */
    private HttpResponse handleMissingPathVariable(MissingPathVariableException ex,
                                                   HttpServletRequest request, HttpResponse response) {

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        return response;
    }

    /**
     * Handle the case when a required parameter is missing.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the MissingServletRequestParameterException
     * could be rethrown as-is.
     *
     * @param ex       the MissingServletRequestParameterException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                              HttpServletRequest request, HttpResponse response) {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return response;
    }

    /**
     * Handle the case when an unrecoverable binding exception occurs - e.g. required header, required cookie.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the exception could be rethrown as-is.
     *
     * @param ex       the exception to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleServletRequestBindingException(ServletRequestBindingException ex,
                                                              HttpServletRequest request, HttpResponse response) {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return response;
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder} conversion cannot occur.
     * <p>The default implementation sends an HTTP 500 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the TypeMismatchException could be rethrown as-is.
     *
     * @param ex       the ConversionNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleConversionNotSupported(ConversionNotSupportedException ex,
                                                      HttpServletRequest request, HttpResponse response) throws IOException {

        if (logger.isWarnEnabled()) {
            logger.warn("Failed to convert request element: " + ex);
        }
        sendServerError(ex, request, response);
        return response;
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder} conversion error occurs.
     * <p>The default implementation sends an HTTP 400 error, and returns an empty {@code ModelAndView}.
     * Alternatively, a fallback view could be chosen, or the TypeMismatchException could be rethrown as-is.
     *
     * @param ex       the TypeMismatchException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     * @return an empty ModelAndView indicating the exception was handled
     * @throws IOException potentially thrown from response.sendError()
     */
    private HttpResponse handleTypeMismatch(TypeMismatchException ex,
                                            HttpServletRequest request, HttpResponse response) throws IOException {

        if (logger.isWarnEnabled()) {
            logger.warn("Failed to bind request element: " + ex);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return response;
    }

    private HttpResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                      HttpServletRequest request, HttpResponse response) throws IOException {

        if (logger.isWarnEnabled()) {
            logger.warn("Failed to read HTTP message: " + ex);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return response;
    }

    private HttpResponse handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                      HttpServletRequest request, HttpResponse response) throws IOException {

        if (logger.isWarnEnabled()) {
            logger.warn("Failed to write HTTP message: " + ex);
        }
        sendServerError(ex, request, response);
        return response;
    }

    private HttpResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request, HttpResponse response) throws IOException {
        BindingResult bindingResult = ex.getBindingResult();
        ResponseData<?> errorMessage = buildErrorMessage(bindingResult);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, Json2.toJson(errorMessage));
        return response;
    }

    private ResponseData<?> buildErrorMessage(BindingResult bindingResult) {
        List<ObjectError> errors = bindingResult.getAllErrors();
        return errors.stream().findFirst().map(error -> {
            String code = error.getCodes() != null ? error.getCodes()[0] : error.getCode();
            ResponseData<Object> responseData = ResponseData.fail(code, error.getDefaultMessage());
            responseData.setRequestId(RequestIdHolder.acquireRequestId());
            return responseData;
        }).get();
    }

    private HttpResponse handleMissingServletRequestPartException(MissingServletRequestPartException ex,
                                                                  HttpServletRequest request, HttpResponse response) throws IOException {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return response;
    }

    private HttpResponse handleBindException(BindException ex, HttpServletRequest request, HttpResponse response) throws IOException {
        BindingResult bindingResult = ex.getBindingResult();
        ResponseData<?> errorMessage = buildErrorMessage(bindingResult);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, Json2.toJson(errorMessage));
        return response;
    }

    private HttpResponse handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                       HttpServletRequest request, HttpResponse response) throws IOException {

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return response;
    }

    private void sendServerError(Exception ex, HttpServletRequest request, HttpResponse response)
            throws IOException {

        request.setAttribute("javax.servlet.error.exception", ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public static class HttpResponse {

        private int status;

        private String code;

        private String message = "";

        private Map<String, String> headers = Maps.newLinkedHashMap();

        private HttpResponse() {
        }

        public HttpResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        private HttpResponse(int status, String code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }

        public static HttpResponse error500(String code, String message) {
            return new HttpResponse(500, code, message);
        }

        public static HttpResponse error400(String code, String message) {
            return new HttpResponse(400, code, message);
        }

        public static HttpResponse error401(String code, String message) {
            return new HttpResponse(401, code, message);
        }

        public static HttpResponse error403(String code, String message) {
            return new HttpResponse(403, code, message);
        }

        private void sendError(int status) {
            this.status = status;
        }

        private void sendError(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private void setHeader(String name, String content) {
            headers.put(name, content);
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return Optional.ofNullable(code).orElse(String.valueOf(status));
        }

        public String getMessage() {
            return message;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }
    }
}
