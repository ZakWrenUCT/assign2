
.PHONY: bin data doc out
.SUFFIXES: .java .class

PKG =
BIN = ./bin/
SRC = ./src/
FLAG = -g -d $(BIN) -cp $(SRC)
COMPILE = javac $(FLAG)
EMPTY =
data =
query =
in =
SOURCE = $(subst $(SRC), $(EMPTY), $(wildcard $(SRC)*.java)) #replace ./src/ with all pkg/.java files

ifdef PKG #If PKG is not empty, split the directories and files and make an agregate list of all files
	PACKAGEDIRS = $(addprefix $(SRC), $(PKG))
	PACKAGEFILES = $(subst $(SRC),$(EMPTY),$(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.java)))
	ALL_FILES = $(PACKAGEFILES) $(SOURCE)
else #If PKG is empty, all files are in the src folder
	ALL_FILES = $(SOURCE)
endif

CLASS_FILES = $(ALL_FILES:.java=.class)

all: $(addprefix $(BIN),$(CLASS_FILES)) #set class files dir as ./bin/*class
$(BIN)%.class: $(SRC)%.java
	$(COMPILE) $<

clean :
	rm -rf $(BIN)*

V1=
V2=
V3=
run:
	java -cp ./bin Main ${V1} ${V2} ${V3}
# make run V1=sampleInput100.txt V2=35 V3=out.txt
