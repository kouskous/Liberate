EXT = cpp
CXX = g++
EXEC = exe
 
CXXFLAGS = -Wall -W -pedantic 
LDFLAGS = 
 
 
OBJDIR = obj
SRC := $(shell find -name '*.$(EXT)')
OBJ = $(SRC:.$(EXT)=.o)
OBJ := $(addprefix $(OBJDIR)/, $(OBJ))

all: $(EXEC)
	@rm -rf $(OBJDIR)
 
$(EXEC): $(OBJ)
	@$(CXX) -o $@ $^ $(LDFLAGS)

$(OBJDIR)/%.o: %.$(EXT)
	- mkdir -p $(@D)
	@$(CXX) -o $@ -c $< $(CXXFLAGS)
 
clean:
	@rm -rf $(OBJDIR)
	@rm -f $(EXEC)
 
install: $(EXEC)
	@cp $(EXEC) /usr/bin/