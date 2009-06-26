#include <stdio.h>


double data_memory[2048];

const int op_add = 0x1;
const int op_sub = 0x2;
const int op_mult = 0x3;
const int op_div = 0x4;
const int op_output = 0x5;
const int op_phi = 0x6;

int main(int argc, char *argv[])
{
  printf("The best damned VM eva!\n");
  return 0;
}

int add(int addr1, int addr2)
{
  return (data_memory[addr1] + data_memory[addr2]);
}

int sub(int addr1, int addr2)
{
  return (data_memory[addr1] - data_memory[addr2]);
}

int mult(int addr1, int addr2)
{
  return (data_memory[addr1] * data_memory[addr2]);
}

int div(int addr1, int addr2)
{
  return (data_memory[addr1] / data_memory[addr2]);
}

