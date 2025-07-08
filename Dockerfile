# Multi-stage Dockerfile for Test Automation Framework
FROM maven:3.9.5-openjdk-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean compile test-compile -B

# Runtime stage
FROM openjdk:21-jre-slim

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Install ChromeDriver
RUN CHROME_VERSION=$(google-chrome --version | awk '{print $3}' | awk -F'.' '{print $1}') \
    && wget -q "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${CHROME_VERSION}" -O /tmp/chromedriver_version \
    && CHROMEDRIVER_VERSION=$(cat /tmp/chromedriver_version) \
    && wget -q "https://chromedriver.storage.googleapis.com/${CHROMEDRIVER_VERSION}/chromedriver_linux64.zip" -O /tmp/chromedriver.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip /tmp/chromedriver_version \
    && chmod +x /usr/local/bin/chromedriver

# Set working directory
WORKDIR /app

# Copy built artifacts from builder stage
COPY --from=builder /app/target ./target
COPY --from=builder /app/src/main/resources ./src/main/resources

# Create directories for reports and screenshots
RUN mkdir -p /app/target/reports /app/target/screenshots /app/target/logs

# Set environment variables
ENV JAVA_OPTS="-Xmx2g -Xms1g"
ENV MAVEN_OPTS="-Xmx2g -Xms1g"
ENV DISPLAY=:99

# Create non-root user
RUN useradd -m -u 1000 testuser && chown -R testuser:testuser /app
USER testuser

# Expose ports for reports (if needed)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Default command
CMD ["mvn", "test"] 