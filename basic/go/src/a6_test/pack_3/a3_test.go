package pack_3

import "testing"

func TestFail(t *testing.T) {
	got := Add(1)
	if got != 3 {
		t.Errorf("got %d, want 1", got)
	}
	t.Log("success work: TestAddFail")
}

func TestFatal(t *testing.T) {
	t.Fatal("test fatal!!!")
}