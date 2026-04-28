# Variables
JAVAC = javac
JAVA = java
FX_PATH = jfx/lib
FX_MODULES = javafx.controls,javafx.fxml
MYSQL_JAR = mysql-connector-j-8.3.0.jar
SRC_DIR = Lab03SRC
PACKAGE = Lab03SRC
MAIN_CLASS = $(PACKAGE).Main

# Find all Java files in the source directory
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Compilation Target
compile:
	@echo "Cleaning old class files..."
	rm -f $(SRC_DIR)/*.class
	@echo "Compiling Java sources..."
	$(JAVAC) --module-path $(FX_PATH) --add-modules $(FX_MODULES) $(SOURCES)

# Run Target
run:
	@echo "Launching Application..."
	$(JAVA) --module-path $(FX_PATH) --add-modules $(FX_MODULES) \
	-cp .:$(MYSQL_JAR) $(MAIN_CLASS)

# Clean Target
clean:
	@echo "Removing class files..."
	rm -f $(SRC_DIR)/*.class

# Setup Database (Utility target for your SQL steps)
db-setup:
	@echo "Opening MySQL terminal..."
	mysql -u root -p
