 🏛️ Университетская система обработки данных

## 📌 Описание
Комплексная система для автоматической обработки данных университетов: чтение из Excel, сортировка, сбор статистики и экспорт в XML/JSON форматы.

## 🛠️ Технологии
- **Java 17**
- **Apache POI** - чтение Excel
- **JAXB** - XML маршаллинг
- **Google Gson** - JSON сериализация
- **JUL** - логирование
- **Maven** - сборка

## 🚀 Быстрый старт
```bash
# Клонировать репозиторий
git clone <repo-url>

# Собрать проект
mvn clean package

# Запустить приложение
mvn exec:java -Dexec.mainClass="org.example.app.UniversityApp"
📁 Структура проекта
text
src/main/java/
├── org.example.app/         # Главное приложение
├── org.example.model/       # Модели данных
├── org.example.service/     # Сервисный слой
├── org.example.util/        # Утилиты
└── org.example.comparator/  # Компараторы
📊 Функционал
✅ Чтение данных из Excel (.xlsx)

✅ Сортировка студентов и университетов

✅ Сбор статистики по профилям обучения

✅ Экспорт в XML (JAXB)

✅ Экспорт в JSON (Gson)

✅ Экспорт в Excel (POI)

✅ Автоматическое логирование

✅ Архивация результатов

📂 Выходные файлы
text
exports/
├── xml/      # XML файлы
├── json/     # JSON файлы
├── excel/    # Excel отчеты
└── archive/  # Архивированные копии
🔧 Конфигурация
Входной файл: data/universities.xlsx

Настройки логирования: logs/university_processor_*.log

📝 Логирование
Детальное логирование всех операций

Ротация лог-файлов

Различные уровни: INFO, FINE, SEVERE

🤝 Вклад
PR приветствуются! Для крупных изменений создайте issue.


---



```markdown
# 🏛️ University Data Processing System

## 📌 Description
Comprehensive system for automated university data processing: Excel reading, sorting, statistics collection, and XML/JSON export.

## 🛠️ Tech Stack
- **Java 17**
- **Apache POI** - Excel reading
- **JAXB** - XML marshalling
- **Google Gson** - JSON serialization
- **JUL** - Logging
- **Maven** - Build tool

## 🚀 Quick Start
```bash
# Clone repository
git clone <repo-url>

# Build project
mvn clean package

# Run application
mvn exec:java -Dexec.mainClass="org.example.app.UniversityApp"
📁 Project Structure
text
src/main/java/
├── org.example.app/         # Main application
├── org.example.model/       # Data models
├── org.example.service/     # Service layer
├── org.example.util/        # Utilities
└── org.example.comparator/  # Comparators
📊 Features
✅ Read data from Excel (.xlsx)

✅ Sort students and universities

✅ Collect statistics by study profiles

✅ Export to XML (JAXB)

✅ Export to JSON (Gson)

✅ Export to Excel (POI)

✅ Automatic logging

✅ Results archiving

📂 Output Files
text
exports/
├── xml/      # XML files
├── json/     # JSON files
├── excel/    # Excel reports
└── archive/  # Archived copies
🔧 Configuration
Input file: data/universities.xlsx

Logging settings: logs/university_processor_*.log

📝 Logging
Detailed logging of all operations

Log rotation

Different levels: INFO, FINE, SEVERE

🤝 Contributing
PRs are welcome! For major changes, please open an issue first.
