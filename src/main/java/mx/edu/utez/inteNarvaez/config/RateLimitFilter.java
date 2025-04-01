package mx.edu.utez.inteNarvaez.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    public static final int SC_TOO_MANY_REQUESTS = 429;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ip = httpRequest.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
            httpResponse.getWriter().write("Has alcanzado el limite de intentos de favor intentalo mas tarde.");
        }
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(30, Refill.intervally(5, Duration.ofMinutes(3)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}
