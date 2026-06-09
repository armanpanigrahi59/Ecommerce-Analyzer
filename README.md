# 🛒 PRICE VERSE - E-Commerce Product Comparison Analyzer

A powerful JavaFX desktop application that compares product prices, ratings, and offers across multiple Indian e-commerce platforms using AI-powered web scraping.

![App Screenshot](docs/screenshot.png)

## ✨ Features

- 🤖 **AI-Powered Scraping**: Uses Google Gemini API to fetch real-time product data
- 🏪 **Multi-Platform Support**: Compares prices from Amazon, Flipkart, Meesho, Shopify, Myntra, Ajio, Snapdeal, Tata CLiQ
- 📊 **Smart Insights**: Displays best price, top-rated platform, and fastest delivery
- 📈 **Price Tracking**: Historical price trends with interactive charts
- 🎨 **Dual Themes**: Modern dark and light themes
- 💾 **Database Storage**: PostgreSQL integration for data persistence
- 📑 **Export Options**: Export comparisons to PDF and Excel
- 🔄 **Auto-Refresh**: Automatic product data updates

## 🖥️ Screenshots

### Dark Theme
![Dark Theme](docs/dark-theme.png)

### Light Theme
![Light Theme](docs/light-theme.png)

### Product Comparison
![Comparison Table](docs/comparison-table.png)

## 🚀 Getting Started

### Prerequisites

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **PostgreSQL 13+** ([Download](https://www.postgresql.org/download/))
- **Google Gemini API Key** ([Get Free Key](https://ai.google.dev))

### Installation

1. **Clone the repository**
git clone https://github.com/armanpanigrahi59/ecommerce-analyzer.git
cd ecommerce-analyzer
2. **Set up PostgreSQL Database**
Open psql
psql -U postgres

Create database
CREATE DATABASE ecommerce_analyzer;
Copy example configuration
cp src/main/resources/application.properties.example src/main/resources/application.properties

Edit with your credentials
nano src/main/resources/application.properties
Add your credentials:
gemini.api.key=YOUR_ACTUAL_API_KEY
db.password=YOUR_DB_PASSWORD
4. **Build and Run**
Build project
./gradlew build

Run application
./gradlew run
## 📁 Project Structure

ecommerce-analyzer/
├── src/main/
│ ├── java/com/ecommerce/analyzer/
│ │ ├── controller/ # JavaFX Controllers
│ │ ├── model/ # Data Models
│ │ ├── repository/ # Database Access Layer
│ │ ├── service/ # Business Logic
│ │ ├── util/ # Utility Classes
│ │ └── Main.java # Application Entry Point
│ └── resources/
│ ├── fxml/ # JavaFX UI Files
│ ├── css/ # Stylesheets
│ └── application.properties
├── database/
│ └── schema.sql # Database Schema
├── docs/ # Documentation & Screenshots
├── build.gradle # Gradle Configuration
└── README.md

### Database Connection
db.url=jdbc:postgresql://localhost:5432/ecommerce_analyzer
db.username=postgres
db.password=your_password


## 👨‍💻 Author

**Arman Panigrahi**
- GitHub: [@armanpanigrahi59](https://github.com/armanpanigrahi59)



## 🙏 Acknowledgments

- Google Gemini API for AI capabilities
- JavaFX community for UI components
- All open-source library contributors
