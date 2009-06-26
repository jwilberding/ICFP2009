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

void noop(int addr)
{
  printf("noop\n");
}

void cmpz(int addr)
{
  printf("cmpz not implemented yet!\n");
  status = 0;
}

// sqrt already used by math.h

double sqrt2(int addr)
{
  return sqrt(data_memory[addr]);
}

double copy(int addr)
{
  return data_memory[addr];
}

double input(int addr)
{
  printf("input not implemented yet!\n");
  return 0;
}

// Comparison operators

void ltz(int addr)
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

void lte(int addr)
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

void eqz(int addr)
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

void gez(int addr)
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

void gtz(int addr)
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

print_hex(char c)
{
  printf( "%02X", (unsigned char) c );
}

void execute_s_type(char instruction[4])
{
  int op_code;

  op_code = (instruction[3] & 0x0F);

  printf("op code: %d\n", op_code);
}

void execute_d_type(char instruction[4])
{
  int op_code;

  op_code = (instruction[3] & 0xF0);

  printf("op code: %d\n", op_code);
}

read_instruction(FILE *f)
{
  char instruction[4]; // 32-bits 
  fread(&instruction, 4, 1, f);
  
  print_hex(instruction[3]);
  print_hex(instruction[2]);
  print_hex(instruction[1]);
  print_hex(instruction[0]);
  printf("\n");

  if ((instruction[3] & 0xF0) == 0)
  {
    // S-type
    printf("s type\n");
    execute_s_type(instruction);
  }
  else
  {
    // D-type
    printf("d type\n");
    execute_d_type(instruction);
  }
}

int main(int argc, char *argv[])
{
  FILE *f;

  printf("C.R.E.A.VM.!\n\n");

  if (argc < 2)
  {
    printf("NO ARGUMENT SPECIFIED, SHAME ON A NIGGA\n");
    printf("usage: %s obf_file\n", argv[0]);
    return -1;
  }

  f = fopen(argv[1], "r");
  
  //while (!feof(f))
  {
    read_instruction(f);
    read_instruction(f);
    read_instruction(f);
    read_instruction(f);
  }

  fclose(f);

  return 0;
}


