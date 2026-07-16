# ChatBot Application

A sophisticated AI-powered chatbot application built with Spring Boot, LangChain4j, and LangGraph4j. This application features Retrieval-Augmented Generation (RAG), conversation memory, JWT authentication, and document processing capabilities.
## Overview

This chatbot application provides an intelligent conversational interface that can:
- Remember conversation history
- Retrieve information from uploaded documents (RAG)
- Use tools for enhanced capabilities
- Maintain secure user authentication with JWT
- Process various document formats (PDF, DOCX, TXT, MD)
- Provide detailed logging and monitoring

##  Architecture

The application follows a modular, layered architecture:

```
com.chatbot
├── controller          # REST API controllers
├── service             # Business logic services
├── repository          # Data access layer
├── model               # Data transfer objects and entities
├── exception           # Custom exception handling
├── config              # Configuration classes
├── security            # JWT authentication and security config
├── util                # Utility classes
├── ai                  # AI/LangGraph components
│   ├── graph           # LangGraph workflow definition
│   ├── provider        # AI provider abstractions
│   ├── state           # State management for LangGraph
│   └── node            # Individual nodes in the AI graph
└── util/rag            # Retrieval-Augmented Generation components
```

### Core Components

1. **AI Processing Pipeline** (`com.chatbot.ai.graph.ChatGraph`)
   - Built with LangGraph4j for workflow orchestration
   - Implements a sequential flow: Memory → Retrieval → Prompt → LLM → Response
   - Uses asynchronous node actions for non-blocking execution

2. **State Management** (`com.chatbot.ai.state.ChatState`)
   - Maintains conversation context throughout the AI processing pipeline
   - Tracks conversation history, user messages, tool results, and retrieved context
   - Manages token usage statistics

3. **Prompt Engineering** (`com.chatbot.ai.state.PromptTemplate`)
   - Constructs sophisticated prompts with clear priority ordering:
     1. Current user message
     2. Conversation history
     3. Tool results
     4. Knowledge base (RAG)
     5. General knowledge
   - Includes specific guidelines for handling user personal information

4. **Retrieval-Augmented Generation** (`com.chatbot.util.rag.*`)
   - Document processing pipeline for multiple formats
   - Text chunking and embedding generation
   - Vector storage using pgvector extension

5. **Security** (`com.chatbot.security.*`)
   - JWT-based authentication
   - Role-based access control
   - Password encoding with BCrypt

##  Features

- **Conversational Memory**: Maintains chat history for contextual understanding
- **Document Processing**: Upload and query PDF, DOCX, TXT, and MD files
- **Retrieval-Augmented Generation**: Ground responses in uploaded documents
- **Tool Integration**: Extensible tool system for external data retrieval
- **User Authentication**: Secure JWT-based user management
- **Role-Based Access**: Different permissions for users and administrators
- **Comprehensive Logging**: Detailed audit trails with correlation IDs
- **API Documentation**: Auto-generated Swagger/OpenAPI documentation
- **Database Persistence**: PostgreSQL with Hibernate/JPA
- **Vector Search**: pgvector embeddings for semantic search
- **Monitoring**: Spring Boot Actuator integration ready

##  Technology Stack

### Core Framework
- **Spring Boot 3.5.4** - Application framework
- **Java 21** - Language level
- **Spring Data JPA** - ORM and data access
- **Spring Security** - Authentication and authorization
- **Spring Web MVC** - REST API layer

### AI/ML Components
- **LangChain4j 1.4.0** - LLM orchestration and tools
- **LangGraph4j 1.8.17** - Workflow/graph-based AI orchestration
- **Ollama** - Local LLM inference (qwen2.5:0.5b)
- **Nomic Embed Text** - Embedding model for RAG
- **pgvector** - PostgreSQL extension for vector similarity search

### Document Processing
- **Apache PDFBox 3.0.5** - PDF text extraction
- **Apache POI 5.2.5** - DOCX text processing
- **Tika** - Additional document format support (indirect)

### Security & Utilities
- **JJWT 0.12.6** - JSON Web Token implementation
- **Lombok** - Boilerplate code reduction
- **SpringDoc OpenAPI 2.8.9** - API documentation
- **SLF4J** - Logging facade

### Development Tools
- **Maven** - Build and dependency management
- **PostgreSQL** - Relational database
- **Lombok** - Reduces boilerplate code

##  Project Structure

```
chatbot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/chatbot/
│   │   │       ├── controller/     # REST controllers
        │   │   │   │   ├── AuthController.java
        │   │   │   │   ├── ChatController.java
        │   │   │   │   ├── ConversationController.java
        │   │   │   │   ├── KnowledgeController.java
        │   │   │   │   └── MessageController.java
        │   │   │   ├── service/    # Business logic
        │   │   │   │   ├── impl/   # Service implementations
        │   │   │   │   │   ├── AuthServiceImpl.java
        │   │   │   │   │   ├── ChatServiceImpl.java
        │   │   │   │   │   ├── ConversationServiceImpl.java
        │   │   │   │   │   ├── MessageServiceImpl.java
        │   │   │   │   │   └── UserServiceImpl.java
        │   │   │   │   ├── AuthService.java
        │   │   │   │   ├── ChatService.java
        │   │   │   │   ├── ConversationService.java
        │   │   │   │   ├── MessageService.java
        │   │   │   │   └── UserService.java
        │   │   │   ├── repository/ # Data access
        │   │   │   │   ├── ConversationRepository.java
        │   │   │   │   ├── MessageRepository.java
        │   │   │   │   ├── UserRepository.java
        │   │   │   │   └── rag/    # RAG-specific repositories
        │   │   │   │       ├── DocumentChunkRepo.java
        │   │   │   │       └── DocumentKnowledgeRepo.java
        │   │   │   ├── model/      # DTOs and entities
        │   │   │   │   ├── chatbot/    # Chat-specific models
        │   │   │   │   │   ├── ChatRequest.java
        │   │   │   │   │   ├── ChatResponse.java
        │   │   │   │   │   ├── ConversationMemory.java
        │   │   │   │   │   └── Conversations.java
        │   │   │   │   ├── rag/      # RAG models
        │   │   │   │   │   ├── DocumentChunk.java
        │   │   │   │   │   └── KnowledgeDocument.java
        │   │   │   │   ├── RegisterRequest.java
        │   │   │   │   ├── LoginRequest.java
        │   │   │   │   └── LoginResponse.java
        │   │   │   ├── exception/  # Custom exceptions
        │   │   │   │   ├── GlobalException.java
        │   │   │   │   ├── ResourceNotFoundException.java
        │   │   │   │   └── UserAlreadyExistsException.java
        │   │   │   ├── config/     # Configuration classes
        │   │   │   │   ├── OllamaConfig.java
        │   │   │   │   └── RagConfig.java
        │   │   │   ├── security/   # Security components
        │   │   │   │   ├── JwtUtils.java
        │   │   │   │   ├── JwtAuthenticationFilter.java
        │   │   │   │   └── WebSecurityConfiguration.java
        │   │   │   ├── util/       # Utility classes
        │   │   │   │   ├── ChatOrchestrator.java
        │   │   │   │   ├── DocumentStatus.java
        │   │   │   │   ├── LoggingAspect.java
        │   │   │   │   ├── MessageRole.java
        │   │   │   │   ├── Role.java
        │   │   │   │   ├── SwaggerConfig.java
        │   │   │   │   └── rag/    # RAG utilities
        │   │   │   │       ├── DocxTextExtractor.java
        │   │   │   │       ├── DocumentProcessor.java
        │   │   │   │       ├── MarkdownTextExtractor.java
        │   │   │   │       ├── PdfTextExtractor.java
        │   │   │   │       ├── TextExtractor.java
        │   │   │   │       └── TxtTextExtractor.java
        │   │   │   └── ai/         # AI/LangGraph components
        │   │   │       ├── graph/      # Workflow definition
        │   │   │   │   ├── ChatGraph.java
        │   │   │   │   └── GraphConfig.java
        │   │   │       ├── provider/   # AI provider abstractions
        │   │   │   │   ├── AiProvider.java
        │   │   │   │   └── OllamaProvider.java
        │   │   │       ├── state/      # State management
        │   │   │   │   ├── ChatState.java
        │   │   │   │   └── PromptTemplate.java
        │   │   │       └── node/       # Individual graph nodes
        │   │   │           ├── LlmNode.java
        │   │   │           ├── MemoryNode.java
        │   │   │           ├── ResponseNode.java
        │   │   │           ├── PromptNode.java
        │   │   │           └── RetrievalNode.java
        │   │   └── resources/
        │   │       └── application.properties
        │   └── test/
        │       └── java/com/chatbot/ChatbotApplicationTests.java
        └── pom.xml
```

##  Getting Started

### Prerequisites
- Java 21 JDK
- Maven 3.8+
- PostgreSQL 12+
- Ollama (for local LLM inference)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd chatbot
   ```

2. **Set up PostgreSQL database**
   ```bash
   createdb chatbot_db
   # Update credentials in src/main/resources/application.properties if needed
   ```

3. **Install and configure Ollama**
   ```bash
   # Install Ollama (https://ollama.com/download)
   ollama pull qwen2.5:0.5b
   ollama pull nomic-embed-text
   ```

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Or run the generated JAR:
   ```bash
   java -jar target/chatbot-0.0.1-SNAPSHOT.jar
   ```

##  Configuration

### Application Properties (`src/main/resources/application.properties`)

### Required Database Extensions

The application requires the `pgvector` extension for vector similarity search:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

##  API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Authenticate user and get JWT token

### Chat Operations
- `POST /api/v1/chat` - Send a message and get AI response
  - Requires Authorization: Bearer <token> header
  - Request body: `{ "message": "your message", "chatId": null (for new chat) }`

### Conversation Management
- `GET /api/v1/conversations` - Get user's conversations
- `GET /api/v1/conversations/{id}` - Get conversation by ID
- `DELETE /api/v1/conversations/{id}` - Delete conversation

### Message Management
- `GET /api/v1/messages/conversation/{conversationId}` - Get messages for a conversation

### Knowledge Base
- `POST /api/v1/knowledge/upload` - Upload document for RAG
- `GET /api/v1/knowledge/documents` - List uploaded documents
- `DELETE /api/v1/knowledge/documents/{id}` - Delete document

### Health & Monitoring
- `GET /actuator/health` - Health check endpoint
- `GET /swagger-ui.html` - Swagger UI documentation

##  AI Pipeline

The AI processing follows this sequence:

1. **Memory Node** (`MemoryNode`)
   - Loads conversation history from database
   - Prepares contextual information

2. **Retrieval Node** (`RetrievalNode`)
   - Searches knowledge base for relevant document chunks
   - Uses semantic search via pgvector embeddings
   - Retrieves top-k most relevant passages

3. **Prompt Node** (`PromptTemplate`)
   - Constructs comprehensive prompt with:
     - System instructions
     - Current date
     - Retrieved knowledge (RAG)
     - Conversation history
     - Tool results (if any)
     - User's current question
   - Follows strict priority ordering for information sources

4. **LLM Node** (`OllamaProvider`)
   - Sends constructed prompt to Ollama LLM
   - Uses qwen2.5:0.5b model for generation
   - Tracks token usage (prompt/completion/total)

5. **Response Node** (`ResponseNode`)
   - Cleans and formats LLM response
   - Trims whitespace
   - Prepares final response

### Prompt Engineering Principles

The prompt template follows these guidelines:

1. **Priority Order** for information sources:
   - Current user message (highest priority)
   - Conversation history
   - Tool results
   - Knowledge base (RAG)
   - General knowledge (lowest priority)

2. **Personal Information Handling**:
   - Never use knowledge base to answer personal questions
   - Only answer from conversation history
   - Explicitly state uncertainty when information is unavailable

3. **Response Quality**:
   - Cite sources when using knowledge base
   - Admit uncertainty when appropriate
   - Keep responses concise unless details requested
   - Use markdown formatting for readability
   - Maintain polite, helpful, conversational tone

##  Security

### Authentication
- JWT-based stateless authentication
- Passwords encrypted with BCrypt
- Token expiration configurable (default .5 hour)
- Refresh token mechanism not implemented (access tokens only)

### Authorization
- Role-based access control (USER, ADMIN roles)
- Protected endpoints require valid JWT
- Public endpoints: registration, login, health checks

### Security Headers
- Basic HTTP security headers enabled
- CORS configuration restricted to trusted origins

##  Database Schema

### Core Tables
- `users` - User accounts with hashed passwords
- `conversations` - Chat sessions linked to users
- `messages` - Individual chat messages with role (user/assistant)
- `knowledge_documents` - Uploaded documents metadata
- `document_chunks` - Document segments with embeddings for RAG

### Relationships
- User 1:* Conversations
- Conversation 1:* Messages
- KnowledgeDocument 1:* DocumentChunks

### Indexes
- Primary keys on all ID columns
- Foreign key constraints for relationships
- GIN index on document_chunks embedding column for fast similarity search

##  Running the Application

### Development Mode
```bash
mvn spring-boot:run
```

### Production Mode
```bash
# Build executable JAR
mvn clean package -DskipTests

# Run the application
java -jar target/chatbot-0.0.1-SNAPSHOT.jar
```

### Docker (Optional)
```bash
# Build Docker image
docker build -t chatbot-app .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/chatbot_db \
  -e SPRING_DATASOURCE_USERNAME=samareshmaiti \
  -e SPRING_DATASOURCE_PASSWORD=samaresh@admin \
  -e OLLAMA_BASE_URL=http://host.docker.internal:11434 \
  chatbot-app
```

## API Documentation

Once the application is running, access the interactive API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

##  Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
The application includes basic integration tests for:
- Authentication flow
- Chat message processing
- Document upload and retrieval
- Conversation management

##  License

This project is licensed under the MIT License - see the LICENSE file for details.

##  Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

##  Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify PostgreSQL is running
   - Check credentials in application.properties
   - Ensure pgvector extension is installed

2. **Ollama Connection Failed**
   - Verify Ollama service is running (`ollama serve`)
   - Check that required models are pulled (`ollama list`)
   - Verify OLLAMA_BASE_URL in application.properties

3. **Memory Issues with Large Documents**
   - Consider increasing JVM heap size: `-Xmx2g`
   - Document processing happens in chunks to manage memory

4. **Slow Response Times**
   - First request may be slower as LLM model loads
   - Subsequent requests benefit from Ollama's caching
   - Consider adjusting chunk size in RAG configuration

##  Support

For questions and support, please open an issue in the repository.

--- 
*Built with Spring Boot, LangChain4j, and LangGraph4j*