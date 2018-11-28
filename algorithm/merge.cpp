#include <stdio.h>

void 
merge_list(int a[], int first, int mid, int last, int temp[])
{
    int i = first, j = mid + 1;
    int m = mid,   n = last;
    int k = 0;

    while (i <= m && j <= n)
    {
        if (a[i] <= a[j])
            temp[k++] = a[i++];
        else
            temp[k++] = a[j++];
    }

    while (i <= m)
        temp[k++] = a[i++];

    while (j <= n)
        temp[k++] = a[j++];

    for (i = 0; i < k; i++)
        a[first + i] = temp[i];
}

void 
merge_sort(int a[], int first, int last, int temp[])
{
    if (first < last)
    {
        int mid = (first + last) / 2;
        merge_sort(a, first, mid, temp);    //左边有序
        merge_sort(a, mid + 1, last, temp); //右边有序
        merge_list(a, first, mid, last, temp); //再将二个有序数列合并
    }
}

void heap_list(int a[], int i, int nLength)
{
    int nChild;
    int nTemp;

    /** 
	 * 一直遍历到叶子，将较小的值持续下沉
	 */
    for (nTemp = a[i]; 2 * i + 1 < nLength; i = nChild) {
        // 子结点的位置=2*（父结点位置）+ 1
        nChild = 2 * i + 1;
        // 得到子结点中较大的结点
        if (nChild < nLength-1 && a[nChild + 1] > a[nChild]) {
            ++nChild;
        }
        // 如果较大的子结点大于父结点那么把较大的子结点往上移动，替换它的父结点
        if (nTemp < a[nChild]) {
            a[i] = a[nChild];
            a[nChild]= nTemp;
        } else {
        // 否则退出循环
            break;
		}
    }
}

// 堆排序算法
void heap_sort(int a[],int length)
{
    int tmp;
    // 调整序列的前半部分元素，调整完之后第一个元素是序列的最大的元素
    //length/2-1是第一个非叶节点，此处"/"为整除
    for (int i = length / 2 - 1; i >= 0; --i) {
        heap_list(a, i, length);
    }
    // 从最后一个元素开始对序列进行调整，不断的缩小调整的范围直到第一个元素
    for (int i = length - 1; i > 0; --i)
    {
        // 把第一个元素和当前的最后一个元素交换，
        // 保证当前的最后一个位置的元素都是在现在的这个序列之中最大的
      ///  Swap(&a[0], &a[i]);
          tmp = a[i];
          a[i] = a[0];
          a[0] = tmp;
        // 不断缩小调整heap的范围，每一次调整完毕保证第一个元素是当前序列的最大值
        // 除了顶部已经排好序，此时，只需要将顶部的值下沉即可
        heap_list(a, 0, i);
    }
}

int 
main(int argc, char* argv[]) {
	int array[] = {7, 2, 1, 1, 2, 3, 4, 5, 0};
	int len = sizeof(array)/sizeof(array[0]);
	int type = 1;

	switch (type) {
		case 0: {
			int temp[100];
			merge_sort(array, 0, len - 1, temp);
			
		} break;
		case 1: {
			heap_sort(array, len);
		}
    } 

    for(int i = 0; i < len; i++) {
        printf("%d, ", array[i]);
    }
    printf("\n");
}
