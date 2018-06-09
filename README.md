<!DOCTYPE html>
<html>
<head>
  <title>ReinbergCore Application</title>
</head>
<body>
<h1><p> ReinbergCore Application</p></h1>
<br/>
<p><h2> Introduction </h2></p>
<p> ReinbergCore is a Java application that enables to process ChIP-seq and RNA-seq data from .fastq files to bigwig files. The different steps run by the application are: <br/>
<ol>
    <li> Perform quality control </li>
    <li> Perform quality filtering </li>
    <li> Align reads on selected reference genome </li>
    <li> Convert SAM files to BAM files </li>
    <li> Sorting BAM files </li>
    <li> Remove duplicated reads and apply elongation size for ChIP-seq </li>
    <li> Transform BAM files to bigWig files </li>
    <li> Perform input subtraction for ChIP-seq </li>
    <li> (optional) Perform spike-in normalization for ChIP-seq </li>
</ol>
</p>
<br/>
<h2> <p> Requirements and Installation </p><h2>
<h3><p>1. Create a NYU Phoenix account </p></h3>
<p>You can request an account by following instructions specified <a href="https://genome.med.nyu.edu/hpcf/wiki/Manual:Cluster_User_Guide#Requesting_Access_to_the_Cluster">here</a> and you can find the cluster manual <a href="https://genome.med.nyu.edu/hpcf/wiki/Manual:Cluster_User_Guide">here</a>. This page will guide you through all the steps so you <b>do not</b> need to read the manual first.</p>

<h3><p>2. Send your login </p></h3>
<p>For instance my login is descon01. This login defines you as a user of the grid. You use it to connect to the grid and it appears on your terminal as login@phoenix2. I need to allow you to access the different scripts of the pipeline which are on my account. This simplifies the installation and avoid having all users changing the scripts every time that an update is done. You do not have to worry about the pipeline maintenance, I will do it for you.</p>

<h3><p>3. Installation </p></h3>
<p>To run the application, you need to download four files:</br>
<ol>
    <li> The Jar file of the application: <a href="ReinbergCore.jar" download>ReinbergCore.jar</a> </li>
    <li> The Bash script to submit the analysis to the cluster: <a href="pipeline_fastq_to_bigwigs_submission.sh" download> pipeline_fastq_to_bigwigs_submission.sh</a></li>
    <li> The Perl script to submit the analysis to the cluster: <a href="pipeline_fastq_to_bigwigs.pl" download> pipeline_fastq_to_bigwigs.pl</a></li>
    <li> The R package to handle arguments: <a href="Rargs.tar.gz" download> Rargs.tar.gz</a></li>
</ol>
You then need to put all files in the same folder on Phoenix. In the example below, I create a reinbergcore folder. You can choose the name that you want. This folder is from where you will run all your analysis:</br>
</br>
<img src="command1.png" alt="ssh descon01@phoenix.med.nyu.edu"></br>
<br/>
Once connected to phoenix:<br/>
<img src="command2.png" alt="Create folder"><br>
<br/>
Here is an explanation of what the different commands do:<br/>
<ol>
    <li> 'ssh descon01@phoenix.nyu.med.edu': This connects you to the grid via an ssh protocol. </li>
    <li> 'mkdir reinbergcore': Creates the folder reinbergcore. mkdir = make directory. </li>
    <li> 'pwd': Gives the path of the current directory. You need this path to copy your files on the grid. pwd = path of working directory. </li>
</ol>
<br/>
Now go to the folder where your three files were downloaded and copy them to the destination folder on Phoenix (scp = ssh copy):</br>
<img src="command3.png" alt="Copy files"><br/>

Replace descon01 by your ID and /ifs/home/descon01/reinbergcore by your own path. <br/>
<br/>
Connect to Phoenix, go to the folder to which you have just copied the files and run R:<br/>
<img src="runR.png" alt="run R"><br/>
<br/>
Install the Rargs.tar.gz package by running the commands:<br/>
<img src="installRargs.png" alt="Install Rargs"><br/>
<br/>
Run the application by following these steps:
<br/><br/>
<img src="loadApplication.png" alt="Load application"><br/>
Do not forget to replace the commands with your ID and path.<br/>
<br/>
Explanations of the different commands:<br/>
<ol>
    <li> 'ssh descon01@phoenix.nyu.med.edu': This connects you to the grid via an ssh protocol. </li>
    <li> 'qlogin -l mem_free=10G -l h_vmem=10G -l mem_token=10G': qlogin connects you to a node (see section The idea behing the Interface). <b> You should never load the application on the main node!</b></li>
    <li> 'cd /ifs/home/descon01/reinbergcore/': This places you in the directory where you copied your files. cd = current directory. </li>
    <li> 'module load java/1.8': Because you want to run a Java application, you have to specify what version of Java the grid should use. </li>
    <li> 'module load jre/1.8': Same thing for the Java RunTime Environment (Look on the web for more information). </li>
    <li> 'java -jar ReinbergCore.jar': This command loads the application. </li>
</ol>
</p>
<br/>
<br/>
<h2><p>Interface overview <p></h2>
<p>From top to bottom, the interface contains:
<ol>
    <li> Fastq folder field: Click select and browse to the folder containing your fastq files. </li>
    <li> Analysis name: This section is filled automatically after selecting your folder. The last folder name of the path is used as analysis name. This name is used as prefix for the different log reports. </li>
    <li> Choose if your experiments were generated by ChIP-seq or RNA-seq. If selecting ChIP-seq, a fragment size option will appear on the interface </li>
    <li> Select the organism and genome to align the data to. More genomes can be provided on request. </li>
    <li> Indicate if the data are single or paired-ended. Paired-end treatment will be available on request. </li>
    <li> Select the kind of output needed and indicate if the experiments should be spiked-in normalized. </li>
    <li> Select the min and max number of CPU to process the data on (The more you use, faster it is but you can wait longer before your job is submitted). My advice is to use min=1 and max=20. </li>
</ol>
<br/>
<img src="interface.png" alt="Interface overview"><br/>
</p>
<br/>
<br/>
<h2><p> The idea behind the interface </p></h2>

<p> The general principle of this interface is to format information in order to submit all treatments to the grid. When clicking the "Submit" button, three kinds of information are displayed (see details below):
<ol>
    <li> The formated line that will be written to a .conf file in the current folder. This file is the starting point of the treatment. </li>
    <li> The file name is indicated. This file is written in the same folder from which you loaded the application. </li>
    <li> The command submitted to the grid which is of the form: qsub pipeline_fastq_to_bigwigs_submission.sh <i>myinputfile.conf</i></li>
</ol>
<br/>
<img src="submit.png" alt="submit details"><br/>
<br/>
<i><b>qsub</b></i> is a command that submits a job to the queue. A <i>queue</i> is the HyperComputing or grid waiting list. When a job is submitted to the grid, it first goes into a waiting list to be submitted. Once it is the next job to be submitted, it is sent to a <i>node</i>. You can imagine a node as an individual computer that can calculate super fast. Therefore, the above command <i>qsub pipeline_fastq_to_bigwigs_submission.sh myinputfile</i> means that the bash script pipeline_fastq_to_bigwigs_submission.sh is submitted to the queue and that it is taking <i>myinputfile</i> as parameter. <br/>
As you probably noticed, you downloaded a third file called <i>pipeline_fastq_to_bigwigs.pl</i>. It is a Perl script that is called by pipeline_fastq_to_bigwigs_submission.sh which role is to read all parameters of <i>myinputfile.conf</i> and load the proper operations to perform on your fastq files.</br>
The next sections describe each operation and where to find the different outputs and logs of the pipeline.
</p>
<br/>
<br/>
<h2><p> The pipeline step by step</p></h2>
<h3><p> 1. Quality control</p></h3>
<p>The quality of sequencing can affect the downstream analysis of your data. As a rule of thumb, for an Illumina sequencer, you expect that in a particular sequence, you have 80% of the quality scores above 25. <u><b>This rule is used by default by the program.</b></u> <br/>
The first part of the pipeline generates a quality report with <a href="http://www.bioinformatics.babraham.ac.uk/projects/fastqc/">FastQC v0.11.4</a>.<br/>
<br/>
The results of the quality control can be found in the same folder than your fastq files and consist of .html and .zip files. ReinbergCore application output the logs of this quality control in the same folder of your fastq files under <b><i>yourFastqFolder</i>/tmp/scripts/quality_control/</b>. As you can notice, this folder contains three types of files:</br>
<ol>
    <li> A .conf file that contains all the fastq files path. This is given as input to FastQC. </li>
    <li> Some <i>analysisname</i>.o<i>JObID</i>.<i>JobNb</i> files which contain the terminal outputs of the FASTQC program. </li>
        <li> Some <i>analysisname</i>.e<i>JObID</i>.<i>JobNb</i> files which contain the error messages of the FASTQC program if there are. </li>
</ol>

<img src="qualityControlLog.png" alt="Quality control log"><br/><br/>
<b><u><i>All the steps of the pipeline output the same thing. A configuration file (.conf), an output file (.o) and an error file (.e) for each sample. These files can be used to figure out what can be the problem if your data processing fails.</i></u></b>
</p>

<h3><p> 2. Quality filtering</p></h3>
<p> After controlling the sequences quality, ReinbergCore filters out all sequences which do not have at least 80% of their scores above 25. This is achieved by <a href="http://www.nipgr.res.in/ngsqctoolkit.html">NGSQCToolkit v2.3.3</a>. The filtered sequences are written to <b><i>yourFastqfolder</i>/quality_control_and_filtering/IlluQC_Filtered_files/</b>:<br/>
<br/>
<img src="filteredFastq.png" alt="Filtered fastq files"><br/>
<br/>
The png, stat and html files contain information about the filtering. The other files indicate if the transition to the next steps of the pipeline was done properly. The log files of NGSQCToolkit can be found in <b><i>yourFastqfolder</i>/tmp/scripts/quality_filtering/</b>.<br/><br/>
<img src="qualityFilteringLog.png" alt="Quality filtering log"><br/><br/>
</p>

<h3><p> 3. Data alignment</p></h3>
<p> The filtered sequences are then aligned to the reference genome selected in the interface. At the moment <i>hg19</i> for Human and <i>mm10</i> for Mouse are available. If using spiked-in ChIP-seq data, external DNA is aligned to <i>dm3</i> for Drosophila. More genome can be added on request. <br/>
<br/>
The pipeline uses:<br/>
<ol>
    <li> For ChIP-seq data: <a href="">Bowtie1 v1.0.0</a> allowing three mismatches and keeping only uniquely aligned reads. </li>
    <li> For RNA-seq data: <a href=""> Tophat1 v2.0.9 </a> allowing three mismatches. </li>
</ol>
Bowtie1 output SAM files to the folder <b><i>yourFastqfolder</i>/quality_control_and_filtering/IlluQC_Filtered_files/.</b><br/>
<br/>
<img src="samfiles.png" alt="SAM files"><br/>
<br/>
These are converted to BAM files and placed in the folder <b><i>yourFastqfolder</i>/bam_files</b><br/>.
<br/>
<img src="unsortedbam.png" alt="Unsorted BAM files"><br/>
<br/>
The logs of the alignment and conversion can be found in <b><i>yourFastqFolder</i>/tmp/scripts/alignment</b> and <b><i>yourFastqFolder</i>/tmp/scripts/sam_to_bam/</b> respectively.<br/>
Tophat1 output directly BAM files that are also moved to <b><i>yourFastqfolder</i>/bam_files</b> and log files can also be found in <b><i>yourFastqFolder</i>/tmp/scripts/alignment.</b><br/><br/>
More aligners will be proposed upon request and in future versions of the application.<br/>
</p>

<h3><p> 4. BAM sorting</p></h3>
<p> BAM files are sorted with <a href="http://broadinstitute.github.io/picard/"> Picard tools v1.88 </a>. A suffix _SORTED_PICARD_COOR is added to each BAM files:<br/>
<br/>
<img src="sortedbam.png" alt="Sorted BAM files"><br/>
</p>

<h3><p> 5. Conversion to Wiggle files</p></h3>
<p> Data are further processed with <a href="">Pasha⁠ v0.99.21</a>: Briefly, for ChIP-seq data, Pasha removes duplicated reads according to a threshold technique and apply the elongation size given as parameter. More sophisticated approaches are available and might be included in later versions. BAM files reads are piled up into bins <b>(50 bp by default)</b> to obtain wiggle files (also for RNA-seq).<br/>
Results of Pasha processing can be found in <b><i>yourFastqFolder</i>/wig_files/</b><br/>
<br/>
<img src="pasharesults.png" alt="Pasha results"><br/>
</p>

<h3><p> 6. ChIP-seq: If not spiked-in, Input subtraction</p></h3>
<p> The input subtraction occurs in two steps: Scaling of the input and subtraction after scaling of the experiment. This is achieved thanks to the <i>normAndSubtractWIG</i> function of the <a href="">Pash\a⁠</a> package. The suffix <i>Scaled</i> is added to the input and <i>Scaled_BGSub</i> to each subtracted sample.<br/>
</p>

<h3><p> 7. ChIP-seq: Spike-in normalization</p></h3>
<p> The normalization consists of different steps:
<ol>
    <li> RPM scaling </li>
    <li> input DNA subtraction </li>
    <li> RPM scaling reversal </li>
    <li> exogenous DNA scaling </li>
</ol>
A suffix corresponding to each step is added to the wiggle file. Below is an example for one sample:<br/>
<br/>
<img src="spikein.png" alt="Spike-in results"><br/> <br/>
</p>

<h3><p> 8. Conversion to bigwigs</p></h3>
<p> Fixed steps wiggle files are converted to bigwigs with the script <a href="http://hgdownload.soe.ucsc.edu/admin/exe/">wigToBigWig</a> available on the UCSC Genome Browser website.<br/>
<br/>
<img src="bigwigs.png" alt="Bigwig files"><br/> <br/>
</p>


<br/>
<br/>
<h2><p> Monitoring processes</p></h2>
<p> The complete execution of the pipeline can take several hours. If one wishes to control at what point the pipeline is you can enter the command:<br/><br/>
<img src="qstat.png" alt="Qstat monitoring"><br/><br/>
<br/>
The first column indicates the jobID, the second columns indicates the priority, the third indicates the job name, the fourth the user and the fifth the status of the job. The status possible values are:<br/>
<ol>
    <li> t(ransferring): A transition like during job-start. </li>
    <li> r: Running. </li>
    <li> s: Suspended. </li>
    <li> hqw: On hold in the queue. </li>
    <li> qw: Means waiting in the queue </li>
    <li> dr: Means the job is ending </li>
</ol>
</p>


