#include <cstdlib>
#include <map>
#include <unordered_set>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

int main() {
  FILE * fin;
  FILE * fout;
  fin = fopen("start.in", "r");
  fout = fopen("start.out", "w");
  
  int n, m;
  fscanf(fin, "%d %d", &m, &n);
  int transfers[n][2];
  char outs[n];
  for (int i = 0; i < n; i++) {
    int t0, t1;
    char c;
    fscanf(fin, "%d %d %c", &t0, &t1, &c);
    transfers[i][0] = t0 - 1;
    transfers[i][1] = t1 - 1;
    outs[i] = c;
  }

  vector<int> reverse[n];
  for (int i = 0; i < n; i++) {
    reverse[transfers[i][0]].push_back(i);
    reverse[transfers[i][1]].push_back(i);
  }

  char word[m + 100];
  fscanf(fin, "%s",  word);
  
  bool used[n];
  fill(used, used + sizeof(used), true);
  bool aux[n];
  for (int i = m - 1; i >= 0; i--) {
    fill(aux, aux + sizeof(aux), false);
    for (int j = 0; j < n; j++) {
      if (used[j] && outs[j] == word[i]) {
	for (int to : reverse[j]) {
	  aux[to] = true;
	}
      }
    }
    memcpy(&used, &aux, sizeof(used));
  }
  vector<int> ans;
  for (int i = 0; i < n; i++) {
    if (used[i]) {
      ans.push_back(i + 1);
    }
  }
  
  sort(ans.begin(), ans.end());
  fprintf(fout, "%d ", ans.size());
  for (int st : ans) {
    fprintf(fout, "%d ", st);
  }
  
  return 0;
}
