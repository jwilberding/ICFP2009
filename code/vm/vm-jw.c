#include <stdio.h>
#include <math.h>

double data_memory[2048];

// D-Type(double arg) instructions

const int dop_add = 0x1;
const int dop_sub = 0x2;
const int dop_mult = 0x3;
const int dop_div = 0x4;
const int dop_output = 0x5;
const int dop_phi = 0x6;

// S-Type(single arg) instructions

const int sop_noop = 0x0;
const int sop_cmpz = 0x1;
const int sop_sqrt = 0x2;
const int sop_copy = 0x3;
const int sop_input = 0x4;

// Comparison operators

const int cop_ltz = 0x0;
const int cop_lez = 0x1;
const int cop_eqz = 0x2;
const int cop_gez = 0x3;
const int cop_gtz = 0x4;

int status = 0;

int main(int argc, char *argv[])
{
  printf("The best damned VM eva!\n");
  return 0;
}

// D-Type(double arg) instructions

double add(int addr1, int addr2)
{
  return (data_memory[addr1] + data_memory[addr2]);
}

double sub(int addr1, int addr2)
{
  return (data_memory[addr1] - data_memory[addr2]);
}

double mult(int addr1, int addr2)
{
  return (data_memory[addr1] * data_memory[addr2]);
}

double div(int addr1, int addr2)
{
  return (data_memory[addr1] / data_memory[addr2]);
}

double output(int addr1, int addr2)
{
  printf("Output to port: %f\n", data_memory[addr2]);
}

double phi(int addr1, int addr2)
{
  double result;

  if (status == 1)
  {
    result = data_memory[addr1];
  }
  else
  {
    result = data_memory[addr2];
  }
  
  return result;
}

// S-Type(single arg) instructions

void noop(addr)
{
  printf("noop\n");
}

void cmpz(addr)
{
  printf("cmpz not implemented yet!\n");
  status = 0;
}

double sqrt(addr)
{
  return sqrt(data_memory[addr]);
}

double copy(addr)
{
  return data_memory[addr];
}

double input(addr)
{
  printf("input not implemented yet!\n");
  return 0;
}

// Comparison operators

const int cop_ltz = 0x0;
const int cop_lez = 0x1;
const int cop_eqz = 0x2;
const int cop_gez = 0x3;
const int cop_gtz = 0x4;

void ltz(addr)
{
  if (data_memory[addr] < 0)
  {
    status = 1;
  }
  else
  {
    status = 0;
  }
}

void lte(addr)
{
  if (data_memory[addr] <= 0)
  {
    status = 1;
  }
  else
  {
    status = 0;
  }
}

void eqz(addr)
{
  if (data_memory[addr] == 0)
  {
    status = 1;
  }
  else
  {
    status = 0;
  }
}

void gez(addr)
{
  if (data_memory[addr] >= 0)
  {
    status = 1;
  }
  else
  {
    status = 0;
  }
}

void gtz(addr)
{
  if (data_memory[addr] > 0)
  {
    status = 1;
  }
  else
  {
    status = 0;
  }
}
