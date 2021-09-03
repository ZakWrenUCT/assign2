
.PHONY: bin data doc out
.SUFFIXES: .java .class

PKG =
#BIN = ./out/production/assign2/
SRC = ./src/
BIN = ./bin/
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
	java -cp ./bin WordApp ${V1} ${V2} ${V3}
# make run V1=5 V2=5 V3=example_dict.txt
