MF = /tmp/tttManifest

TTT = tictactoe.jar
SRCDIR = tictactoe

JFLAGS = -g
JAVAC = javac -cp ./$(SRCDIR):${CLASSPATH}

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $<

_TTT_SRC = TttApplet.java \
	TttFrame.java \
	TttPanel.java \
	TttGui.java \
	Cell.java \
	CellView.java \
	CellWeight.java \
	CellViewMouseHandler.java \
	CellViewFocusHandler.java \
	CellViewKeyHandler.java

TTT_SRC = $(_TTT_SRC:%=$(SRCDIR)/%)

TTT_CLASSES = $(TTT_SRC:.java=.class)

$(TTT):	$(TTT_SRC) $(TTT_CLASSES)
	rm -f $(MF)
	echo "Main-Class: $(SRCDIR)/TttFrame" > $(MF)
	jar cmf $(MF) $@ $(SRCDIR)/*.class
	rm -f $(MF)

clean:
	rm -f $(TTT) $(SRCDIR)/*.class
