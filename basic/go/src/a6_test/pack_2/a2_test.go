package pack_2

import (
	"os"
	"testing"
)

func TestAdSkip(t *testing.T) {
	if os.Getenv("SOME_ACCESS_TOKEN") == "" {
		t.Skip("skipping test; $SOME_ACCESS_TOKEN not set")
	}
}

func TestCountShort(t *testing.T) {
	if testing.Short() {
		t.Skip("skipping test in short mode")
	}
}