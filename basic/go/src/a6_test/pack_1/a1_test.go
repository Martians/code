package pack_1

import (
	"testing"
)

func TestAdd(t *testing.T) {
	got := Add(1)
	if got != 2 {
		t.Errorf("got %d, want 1", got)
	}
}

func BenchmarkAdd(b *testing.B) {
	for i := 0; i < b.N; i++ {
		Add(2)
	}
}